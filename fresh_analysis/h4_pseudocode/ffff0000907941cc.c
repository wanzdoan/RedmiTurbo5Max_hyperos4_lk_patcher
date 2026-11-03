
int FUN_ffff0000907941cc
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
  
  FUN_ffff00009077e11c
            (s__UFS_TRACE___s__ENTRY__desc_id_0_ffff0000907d04b1,
             s_ufshcd_read_desc_param_ffff0000907e0585,param_2,param_2,param_3,param_4 & 0xff,0,
             param_6);
  if (param_6 < 2) {
    pcVar2 = s__UFS_ERROR___s__INVALID_BUFFER_S_ffff0000907e3c0b;
    uVar4 = 2;
    param_2 = param_6;
  }
  else {
    if (param_2 < 0xff) {
      uVar6 = (&DAT_ffff000090836778)[param_2];
      local_34 = uVar6;
      FUN_ffff00009077e11c
                (s__UFS_TRACE___s__Predefined_size__ffff0000907eeff4,
                 s_ufshcd_read_desc_param_ffff0000907e0585,uVar6,param_2);
      if (param_2 < 0x80) {
        if (1 < uVar6) goto LAB_ffff000090794328;
        pcVar2 = s__UFS_ERROR___s__UNSUPPORTED_desc_ffff0000907f39aa;
        uVar5 = 2;
        uVar3 = param_2;
      }
      else {
        if (uVar6 != 1) {
          if (uVar6 == 0) {
            uVar6 = 0xff;
            local_34 = 0xff;
            FUN_ffff00009077e11c
                      (s__UFS_TRACE___s__Using_MAX_size___ffff0000907fff74,
                       s_ufshcd_read_desc_param_ffff0000907e0585,0xff,param_2);
          }
LAB_ffff000090794328:
          if (param_6 < uVar6) {
            FUN_ffff00009077e11c
                      (s__UFS_TRACE___s__Adjusted_desc_le_ffff0000907dee9c,
                       s_ufshcd_read_desc_param_ffff0000907e0585,uVar6,param_6);
            uVar6 = param_6;
            local_34 = param_6;
          }
          FUN_ffff00009077e11c
                    (s__UFS_TRACE___s__Calling_query__o_ffff0000907ebade,
                     s_ufshcd_read_desc_param_ffff0000907e0585,param_2,uVar6);
          iVar1 = FUN_ffff000090794414(param_1,1,param_2,param_3,param_4,param_5,&local_34);
          FUN_ffff00009077e11c
                    (s__UFS_TRACE___s__Query_returned__r_ffff0000907e3c46,
                     s_ufshcd_read_desc_param_ffff0000907e0585,iVar1,local_34);
          if (iVar1 != 0) {
            FUN_ffff00009077e11c
                      (s__UFS_ERROR___s__QUERY_FAILED__de_ffff0000907dd4df,
                       s_ufshcd_read_desc_param_ffff0000907e0585,param_2,iVar1);
          }
          FUN_ffff00009077e11c
                    (s__UFS_TRACE___s__EXIT__ret__d_ffff0000907fc76c,
                     s_ufshcd_read_desc_param_ffff0000907e0585,iVar1);
          return iVar1;
        }
        pcVar2 = s__UFS_ERROR___s__INVALID_size__u_<_ffff0000907d39b3;
        uVar6 = 2;
        uVar3 = 1;
        uVar5 = param_2;
      }
      FUN_ffff00009077e11c(pcVar2,s_ufshcd_read_desc_param_ffff0000907e0585,uVar3,uVar6,uVar5);
      return -1;
    }
    pcVar2 = s__UFS_ERROR___s__INVALID_desc_id__ffff0000907fe42a;
    uVar4 = 0xfe;
  }
  FUN_ffff00009077e11c(pcVar2,s_ufshcd_read_desc_param_ffff0000907e0585,param_2,uVar4);
  return -1;
}


