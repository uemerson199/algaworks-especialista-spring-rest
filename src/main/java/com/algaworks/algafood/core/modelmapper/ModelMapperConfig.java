package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.domain.model.Endereco;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		
		org.modelmapper.TypeMap<Endereco, EnderecoModel> enderecoToEnderecoModel =
				modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		
		enderecoToEnderecoModel.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(), 
				 (enderecoModelDest, value) -> enderecoModelDest.getCidade().setNomeEstado(value));
		
		return modelMapper;
	}


//	@Bean
//	public ModelMapper modelMapper() {
//		 var modelMapper = new ModelMapper();
//		 
//		 modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//		 	  .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
//		 
//		 return modelMapper;
//	}

	
}
