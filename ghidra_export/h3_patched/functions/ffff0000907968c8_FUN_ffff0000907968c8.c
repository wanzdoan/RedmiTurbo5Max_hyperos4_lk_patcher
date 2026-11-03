// ffff0000907968c8  size=548  FUN_ffff0000907968c8

int FUN_ffff0000907968c8(undefined8 param_1,undefined1 param_2,undefined1 *param_3)

{
  ushort uVar1;
  undefined4 uVar2;
  long lVar3;
  int iVar4;
  ulong uVar5;
  char *pcVar6;
  undefined4 local_74;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 local_50;
  long local_48;
  
  if (param_3 == (undefined1 *)0x0) {
    FUN_ffff00009077de54
              (s___s___null_arg_ffff0000907ffe2f,s_ufs_is_rpmb_key_programmed_ffff0000907fa9d5);
    return -0x16;
  }
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_74 = 0;
  local_50 = 0;
  local_48 = -0xffff6f6f5b30;
  uVar5 = FUN_ffff00009078fca8(param_1,&local_74);
  uVar2 = local_74;
  if ((uVar5 & 1) == 0) {
    return -5;
  }
  FUN_ffff00009077eb88((long)&local_68 + 5,0,0x10);
  lVar3 = local_48;
  local_70 = CONCAT44(uVar2,0xc4);
  local_58 = CONCAT26(0xc,(undefined6)local_58);
  local_50 = CONCAT44(local_50._4_4_,0x200);
  local_60 = 0x20000000001;
  local_68 = CONCAT17(param_2,0xecb50000000001);
  local_58 = local_58 & 0xffffffffffffff00;
  if (local_48 == 0) {
    FUN_ffff00009077de54
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd45b,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000907f205b);
  }
  else {
    FUN_ffff00009077eb88(local_48,0,0x200);
    FUN_ffff00009077e858(lVar3 + 0x1e4,0xffff00009090a700,0x10);
    *(undefined2 *)(lVar3 + 0x1fe) = 0x200;
  }
  iVar4 = FUN_ffff000090790d5c(param_1,&local_70);
  uVar2 = local_74;
  if (iVar4 == 0) {
    FUN_ffff00009077eb88((long)&local_68 + 5,0,0x10);
    local_70 = CONCAT44(uVar2,0xc4);
    local_50 = CONCAT44(local_50._4_4_,0x200);
    local_58 = CONCAT26(0xc,(undefined6)local_58);
    local_68 = CONCAT17(param_2,0xeca20000000002);
    local_58 = local_58 & 0xffffffffffffff00;
    local_60 = 0x20000000001;
    iVar4 = FUN_ffff000090790d5c(param_1,&local_70);
    if (iVar4 == 0) {
      uVar1 = *(ushort *)(local_48 + 0x1fc) >> 8 | *(ushort *)(local_48 + 0x1fc) << 8;
      if (uVar1 == 0) {
        iVar4 = 0;
        *param_3 = 1;
      }
      else if (uVar1 == 7) {
        iVar4 = 0;
        *param_3 = 0;
      }
      else {
        iVar4 = -5;
      }
      goto LAB_ffff000090796a80;
    }
    pcVar6 = s___s___Get_result_response_fail__r_ffff0000907eb98f;
  }
  else {
    pcVar6 = s___s___Sending_Write_counter_read_ffff0000907e3bd3;
  }
  FUN_ffff00009077de54(pcVar6,s_ufs_is_rpmb_key_programmed_ffff0000907fa9d5,iVar4);
LAB_ffff000090796a80:
  FUN_ffff00009078fd78(param_1,local_74);
  return iVar4;
}


