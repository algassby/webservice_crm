/**
 * 
 */
package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.responsedto.FileInfoResponseDto;

/**
 * @author algas
 *
 */
@Mapper(componentModel = "spring")
public interface FileInfoMapper {

	public FileInfoResponseDto fileInfoToFileInfoResponseDto(FileInfo fileInfo);
}
