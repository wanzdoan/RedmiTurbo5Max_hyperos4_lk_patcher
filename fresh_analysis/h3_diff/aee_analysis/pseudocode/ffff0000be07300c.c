
int FUN_ffff0000be07300c
              (undefined8 param_1,uint param_2,char param_3,uint param_4,uint param_5,char param_6,
              byte param_7)

{
  long lVar1;
  long lVar2;
  byte bVar3;
  byte bVar4;
  uint uVar5;
  uint uVar6;
  undefined *puVar7;
  undefined4 uVar8;
  int iVar9;
  undefined8 uVar10;
  ulong uVar11;
  char *pcVar12;
  undefined8 local_90;
  undefined4 local_88;
  undefined4 local_84;
  undefined5 uStack_80;
  undefined4 uStack_7b;
  undefined5 uStack_77;
  undefined2 local_72;
  undefined8 local_70;
  undefined *local_68;
  undefined4 local_44;
  
  uStack_80 = 0;
  uStack_7b = 0;
  uStack_77 = 0;
  local_72 = 0;
  local_90 = 0;
  local_88 = 0;
  local_84 = 0;
  local_44 = 0;
  local_70 = 0;
  local_68 = &DAT_ffff0000be1b4c10;
  uVar10 = FUN_ffff0000be070acc();
  uVar11 = FUN_ffff0000be06da88(uVar10,&local_44);
  uVar8 = local_44;
  if ((uVar11 & 1) == 0) {
    return -5;
  }
  lVar2 = (long)&local_84 + 1;
  FUN_ffff0000be060ae0(lVar2,0,0x10);
  puVar7 = local_68;
  local_90 = CONCAT44(uVar8,0xc4);
  local_88 = 1;
  local_72 = 0xc;
  local_70 = CONCAT44(local_70._4_4_,0x200);
  local_84 = 0xecb500;
  uStack_80 = 1;
  uStack_7b = 2;
  if (local_68 == (undefined *)0x0) {
    FUN_ffff0000be05fd98
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049,
               s_ufshcd_swp_conf_block_write_prep_ffff0000be0bc906);
  }
  else {
    FUN_ffff0000be060ae0(local_68,0,0x200);
    DAT_ffff0000be1b4e80 = param_6 - 1;
    uVar5 = (param_2 & 0xff00ff00) >> 8 | (param_2 & 0xff00ff) << 8;
    *(undefined2 *)(puVar7 + 0x1fe) = 0x600;
    *(undefined4 *)(puVar7 + 0x1f8) = 0x1000000;
    bVar4 = param_3 << 1 | 1;
    *(uint *)(puVar7 + 500) = uVar5 >> 0x10 | uVar5 << 0x10;
    if (DAT_ffff0000be1b4e80 < 2) {
      DAT_ffff0000be1b4e90 = 0;
      if (param_3 != '\x04') {
        DAT_ffff0000be1b4e90 = bVar4;
      }
      DAT_ffff0000be1b4e81 = '\x10';
      DAT_ffff0000be1b4e9c = 0;
      DAT_ffff0000be1b4e94 = 0;
    }
    else {
      lVar1 = (ulong)param_7 * 0x10;
      bVar3 = 0;
      if (param_3 != '\x04') {
        bVar3 = bVar4;
      }
      uVar5 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
      uVar6 = (param_5 & 0xff00ff00) >> 8 | (param_5 & 0xff00ff) << 8;
      DAT_ffff0000be1b4e81 = param_7 * '\x10' + '\x10';
      (&DAT_ffff0000be1b4e90)[lVar1] = bVar3;
      *(uint *)((long)&DAT_ffff0000be1b4e94 + lVar1 + 4) = uVar5 >> 0x10 | uVar5 << 0x10;
      (&DAT_ffff0000be1b4e9c)[(ulong)param_7 * 4] = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    FUN_ffff0000be05fd98
              (s___s___Write_swpcb_ffff0000be0a6532,
               s_ufshcd_swp_conf_block_write_prep_ffff0000be0bc906);
    FUN_ffff0000be0746b8(&DAT_ffff0000be1b4e80);
    FUN_ffff0000be0607b0(puVar7 + 0xe4,&DAT_ffff0000be1b4e80,0x100);
    iVar9 = FUN_ffff0000be074520(puVar7);
    if (iVar9 == 0) {
      FUN_ffff0000be0607b0(puVar7 + 0xc4,DAT_ffff0000be1b4c08,0x20);
    }
    else {
      FUN_ffff0000be05fd98
                (s___s___rpmb_get_mac_fail__ret__d_ffff0000be0c5ac6,
                 s_ufshcd_swp_conf_block_write_prep_ffff0000be0bc906,iVar9);
    }
  }
  iVar9 = FUN_ffff0000be06eb3c(uVar10,&local_90);
  uVar8 = local_44;
  if (iVar9 == 0) {
    FUN_ffff0000be060ae0(lVar2,0,0x10);
    puVar7 = local_68;
    local_90 = CONCAT44(uVar8,0xc4);
    local_88 = 1;
    local_72 = 0xc;
    local_70 = CONCAT44(local_70._4_4_,0x200);
    local_84 = 0xecb500;
    uStack_80 = 1;
    uStack_7b = 2;
    if (local_68 == (undefined *)0x0) {
      FUN_ffff0000be05fd98
                (s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049,
                 s_ufshcd_authen_result_read_req_pr_ffff0000be0bee80);
    }
    else {
      FUN_ffff0000be060ae0(local_68,0,0x200);
      *(undefined2 *)(puVar7 + 0x1fe) = 0x500;
    }
    iVar9 = FUN_ffff0000be06eb3c(uVar10,&local_90);
    uVar8 = local_44;
    if (iVar9 == 0) {
      FUN_ffff0000be060ae0(lVar2,0,0x10);
      local_70 = CONCAT44(local_70._4_4_,0x200);
      local_90 = CONCAT44(uVar8,0xc4);
      local_88 = 2;
      uStack_7b = 2;
      local_72 = 0xc;
      local_84 = 0xeca200;
      uStack_80 = 1;
      iVar9 = FUN_ffff0000be06eb3c(uVar10,&local_90);
      if (iVar9 == 0) {
        iVar9 = FUN_ffff0000be07411c(&local_90,param_2,0x600);
        if (iVar9 == 0) goto LAB_ffff0000be073354;
        pcVar12 = s___s___RPMB_WriteCounter_Read_Res_ffff0000be0b5d64;
      }
      else {
        pcVar12 = s___s___Sending_request_get_result_ffff0000be0bc8c5;
      }
    }
    else {
      pcVar12 = s___s___Sending_result_register_re_ffff0000be0ba761;
    }
  }
  else {
    pcVar12 = s___s___Sending_Secure_Write_Prote_ffff0000be0b4ad7;
  }
  FUN_ffff0000be05fd98(pcVar12,s_rpmb_authen_secure_write_protect_ffff0000be0bdbc3,iVar9);
LAB_ffff0000be073354:
  FUN_ffff0000be06db58(uVar10,local_44);
  return iVar9;
}


