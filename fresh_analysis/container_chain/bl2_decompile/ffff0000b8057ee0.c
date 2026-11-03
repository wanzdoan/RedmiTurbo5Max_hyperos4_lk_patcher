
int FUN_ffff0000b8057ee0
              (undefined8 param_1,uint param_2,undefined4 param_3,uint param_4,undefined8 param_5,
              uint param_6)

{
  int iVar1;
  char *pcVar2;
  uint uVar3;
  undefined8 uVar4;
  uint uVar5;
  uint uVar6;
  uint local_34;
  
  FUN_ffff0000b804c33c
            (s__UFS_TRACE___s__ENTRY__desc_id_0_ffff0000b8085be9,
             s_ufshcd_read_desc_param_ffff0000b808e616,param_2,param_2,param_3,param_4 & 0xff,0,
             param_6);
  if (param_6 < 2) {
    pcVar2 = s__UFS_ERROR___s__INVALID_BUFFER_S_ffff0000b8090422;
    uVar4 = 2;
    param_2 = param_6;
  }
  else {
    if (param_2 < 0xff) {
      uVar6 = (&DAT_ffff0000b81063e8)[param_2];
      local_34 = uVar6;
      FUN_ffff0000b804c33c
                (s__UFS_TRACE___s__Predefined_size__ffff0000b8096b71,
                 s_ufshcd_read_desc_param_ffff0000b808e616,uVar6,param_2);
      if (param_2 < 0x80) {
        if (1 < uVar6) goto LAB_ffff0000b805803c;
        pcVar2 = s__UFS_ERROR___s__UNSUPPORTED_desc_ffff0000b809949f;
        uVar5 = 2;
        uVar3 = param_2;
      }
      else {
        if (uVar6 != 1) {
          if (uVar6 == 0) {
            uVar6 = 0xff;
            local_34 = 0xff;
            FUN_ffff0000b804c33c
                      (s__UFS_TRACE___s__Using_MAX_size___ffff0000b809fdf1,
                       s_ufshcd_read_desc_param_ffff0000b808e616,0xff,param_2);
          }
LAB_ffff0000b805803c:
          if (param_6 < uVar6) {
            FUN_ffff0000b804c33c
                      (s__UFS_TRACE___s__Adjusted_desc_le_ffff0000b808d7ed,
                       s_ufshcd_read_desc_param_ffff0000b808e616,uVar6,param_6);
            uVar6 = param_6;
            local_34 = param_6;
          }
          FUN_ffff0000b804c33c
                    (s__UFS_TRACE___s__Calling_query__o_ffff0000b8094e2c,
                     s_ufshcd_read_desc_param_ffff0000b808e616,param_2,uVar6);
          iVar1 = FUN_ffff0000b8058128(param_1,1,param_2,param_3,param_4,param_5,&local_34);
          FUN_ffff0000b804c33c
                    (s__UFS_TRACE___s__Query_returned__r_ffff0000b809045d,
                     s_ufshcd_read_desc_param_ffff0000b808e616,iVar1,local_34);
          if (iVar1 != 0) {
            FUN_ffff0000b804c33c
                      (s__UFS_ERROR___s__QUERY_FAILED__de_ffff0000b808cbb6,
                       s_ufshcd_read_desc_param_ffff0000b808e616,param_2,iVar1);
          }
          FUN_ffff0000b804c33c
                    (s__UFS_TRACE___s__EXIT__ret__d_ffff0000b809df4e,
                     s_ufshcd_read_desc_param_ffff0000b808e616,iVar1);
          return iVar1;
        }
        pcVar2 = s__UFS_ERROR___s__INVALID_size__u_<_ffff0000b8087a11;
        uVar6 = 2;
        uVar3 = 1;
        uVar5 = param_2;
      }
      FUN_ffff0000b804c33c(pcVar2,s_ufshcd_read_desc_param_ffff0000b808e616,uVar3,uVar6,uVar5);
      return -1;
    }
    pcVar2 = s__UFS_ERROR___s__INVALID_desc_id__ffff0000b809ef2b;
    uVar4 = 0xfe;
  }
  FUN_ffff0000b804c33c(pcVar2,s_ufshcd_read_desc_param_ffff0000b808e616,param_2,uVar4);
  return -1;
}


