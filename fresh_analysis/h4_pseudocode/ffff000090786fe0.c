
int FUN_ffff000090786fe0(undefined8 param_1,uint *param_2)

{
  int iVar1;
  undefined8 local_870;
  undefined8 uStack_868;
  undefined8 local_860;
  undefined8 uStack_858;
  undefined8 local_850;
  undefined8 uStack_848;
  undefined8 local_840;
  undefined8 uStack_838;
  char local_830 [2];
  undefined4 local_82e;
  int local_826;
  
  FUN_ffff00009077ee50(local_830,0,0x800);
  local_840 = 0;
  uStack_838 = 0;
  local_850 = 0;
  uStack_848 = 0;
  local_860 = 0;
  uStack_858 = 0;
  local_870 = 0;
  uStack_868 = 0;
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_GetSecidInfo_ffff0000907f1fa8);
  iVar1 = FUN_ffff0000907cd89c(0,local_830,0x800);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s_PlatformVendorID___0x_x_Counter___ffff0000907f5578,local_830[0],local_830[1],
               local_82e,local_826);
    if ((local_830[0] == '0') || (local_830[0] == '\0')) {
      if (local_830[0] == '\0' && local_826 == 0) {
        iVar1 = 10;
      }
      else {
        *param_2 = local_826 + 0xe;
        if (local_826 - 0x7b3U < 0xfffff7ff) {
          FUN_ffff00009077e11c(s_Error___total_len_of_data_and_si_ffff0000907eb8c5);
          iVar1 = 0xc;
        }
        else {
          FUN_ffff00009077eb20(param_1,local_830);
          FUN_ffff00009077eb20(&local_870,local_830 + *param_2,0x40);
          iVar1 = FUN_ffff0000907cd558(param_1,*param_2,&local_870,0x40);
          if (iVar1 == 0) {
            FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_GetSecidInfo_ffff0000907f1fa8);
            iVar1 = 0;
          }
          else {
            FUN_ffff00009077e11c(s_Error___ecc_verify_fail_ffff0000907ed318);
            iVar1 = 8;
          }
        }
      }
    }
    else {
      iVar1 = 0xb;
    }
  }
  else {
    FUN_ffff00009077e11c(s_ERROR__read_data_and_sign_data_f_ffff0000907d6d55);
  }
  return iVar1;
}


