
int FUN_ffff0000be0742fc(undefined8 param_1,undefined1 param_2,undefined1 *param_3)

{
  ushort uVar1;
  undefined4 uVar2;
  undefined *puVar3;
  int iVar4;
  ulong uVar5;
  char *pcVar6;
  undefined4 local_74;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 local_50;
  undefined *local_48;
  
  if (param_3 == (undefined1 *)0x0) {
    FUN_ffff0000be05fd98
              (s___s___null_arg_ffff0000be0c49fc,s_ufs_is_rpmb_key_programmed_ffff0000be0c144a);
    return -0x16;
  }
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_74 = 0;
  local_50 = 0;
  local_48 = &DAT_ffff0000be1b4c10;
  uVar5 = FUN_ffff0000be06da88(param_1,&local_74);
  uVar2 = local_74;
  if ((uVar5 & 1) == 0) {
    return -5;
  }
  FUN_ffff0000be060ae0((long)&local_68 + 5,0,0x10);
  puVar3 = local_48;
  local_70 = CONCAT44(uVar2,0xc4);
  local_58 = CONCAT26(0xc,(undefined6)local_58);
  local_50 = CONCAT44(local_50._4_4_,0x200);
  local_60 = 0x20000000001;
  local_68 = CONCAT17(param_2,0xecb50000000001);
  local_58 = local_58 & 0xffffffffffffff00;
  if (local_48 == (undefined *)0x0) {
    FUN_ffff0000be05fd98
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000be0bb795);
  }
  else {
    FUN_ffff0000be060ae0(local_48,0,0x200);
    FUN_ffff0000be0607b0(puVar3 + 0x1e4,&DAT_ffff0000be1b4e40,0x10);
    *(undefined2 *)(puVar3 + 0x1fe) = 0x200;
  }
  iVar4 = FUN_ffff0000be06eb3c(param_1,&local_70);
  uVar2 = local_74;
  if (iVar4 == 0) {
    FUN_ffff0000be060ae0((long)&local_68 + 5,0,0x10);
    local_70 = CONCAT44(uVar2,0xc4);
    local_50 = CONCAT44(local_50._4_4_,0x200);
    local_58 = CONCAT26(0xc,(undefined6)local_58);
    local_68 = CONCAT17(param_2,0xeca20000000002);
    local_58 = local_58 & 0xffffffffffffff00;
    local_60 = 0x20000000001;
    iVar4 = FUN_ffff0000be06eb3c(param_1,&local_70);
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
      goto LAB_ffff0000be0744b4;
    }
    pcVar6 = s___s___Get_result_response_fail__r_ffff0000be0b701f;
  }
  else {
    pcVar6 = s___s___Sending_Write_counter_read_ffff0000be0b168b;
  }
  FUN_ffff0000be05fd98(pcVar6,s_ufs_is_rpmb_key_programmed_ffff0000be0c144a,iVar4);
LAB_ffff0000be0744b4:
  FUN_ffff0000be06db58(param_1,local_74);
  return iVar4;
}


