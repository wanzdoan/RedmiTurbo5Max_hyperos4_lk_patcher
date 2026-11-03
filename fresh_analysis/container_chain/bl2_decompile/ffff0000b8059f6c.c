
int FUN_ffff0000b8059f6c(undefined8 param_1,undefined1 param_2,undefined1 *param_3)

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
    FUN_ffff0000b804c33c
              (s___s___null_arg_ffff0000b809fe6f,s_ufs_is_rpmb_key_programmed_ffff0000b809d0ab);
    return -0x16;
  }
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_74 = 0;
  local_50 = 0;
  local_48 = &DAT_ffff0000b81b6950;
  uVar5 = FUN_ffff0000b8053cfc(param_1,&local_74);
  uVar2 = local_74;
  if ((uVar5 & 1) == 0) {
    return -5;
  }
  FUN_ffff0000b804d070((long)&local_68 + 5,0,0x10);
  puVar3 = local_48;
  local_70 = CONCAT44(uVar2,0xc4);
  local_58 = CONCAT26(0xc,(undefined6)local_58);
  local_50 = CONCAT44(local_50._4_4_,0x200);
  local_60 = 0x20000000001;
  local_68 = CONCAT17(param_2,0xecb50000000001);
  local_58 = local_58 & 0xffffffffffffff00;
  if (local_48 == (undefined *)0x0) {
    FUN_ffff0000b804c33c
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec,
               s_ufshcd_authen_wcnt_read_req_prep_ffff0000b8098740);
  }
  else {
    FUN_ffff0000b804d070(local_48,0,0x200);
    FUN_ffff0000b804cd40(puVar3 + 0x1e4,&DAT_ffff0000b81b6b80,0x10);
    *(undefined2 *)(puVar3 + 0x1fe) = 0x200;
  }
  iVar4 = FUN_ffff0000b8054db0(param_1,&local_70);
  uVar2 = local_74;
  if (iVar4 == 0) {
    FUN_ffff0000b804d070((long)&local_68 + 5,0,0x10);
    local_70 = CONCAT44(uVar2,0xc4);
    local_50 = CONCAT44(local_50._4_4_,0x200);
    local_58 = CONCAT26(0xc,(undefined6)local_58);
    local_68 = CONCAT17(param_2,0xeca20000000002);
    local_58 = local_58 & 0xffffffffffffff00;
    local_60 = 0x20000000001;
    iVar4 = FUN_ffff0000b8054db0(param_1,&local_70);
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
      goto LAB_ffff0000b805a124;
    }
    pcVar6 = s___s___Get_result_response_fail__r_ffff0000b8094e77;
  }
  else {
    pcVar6 = s___s___Sending_Write_counter_read_ffff0000b80904cd;
  }
  FUN_ffff0000b804c33c(pcVar6,s_ufs_is_rpmb_key_programmed_ffff0000b809d0ab,iVar4);
LAB_ffff0000b805a124:
  FUN_ffff0000b8053dcc(param_1,local_74);
  return iVar4;
}


