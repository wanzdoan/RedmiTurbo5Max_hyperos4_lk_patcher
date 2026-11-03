
int FUN_ffff000090795898
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
  local_68 = &DAT_ffff00009090a590;
  uVar10 = FUN_ffff000090793148();
  uVar11 = FUN_ffff00009078ff68(uVar10,&local_44);
  uVar8 = local_44;
  if ((uVar11 & 1) == 0) {
    return -5;
  }
  lVar2 = (long)&local_84 + 1;
  FUN_ffff00009077ee50(lVar2,0,0x10);
  puVar7 = local_68;
  local_90 = CONCAT44(uVar8,0xc4);
  local_88 = 1;
  local_72 = 0xc;
  local_70 = CONCAT44(local_70._4_4_,0x200);
  local_84 = 0xecb500;
  uStack_80 = 1;
  uStack_7b = 2;
  if (local_68 == (undefined *)0x0) {
    FUN_ffff00009077e11c
              (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd515,
               s_ufshcd_swp_conf_block_write_prep_ffff0000907f3a94);
  }
  else {
    FUN_ffff00009077ee50(local_68,0,0x200);
    DAT_ffff00009090a800 = param_6 - 1;
    uVar5 = (param_2 & 0xff00ff00) >> 8 | (param_2 & 0xff00ff) << 8;
    *(undefined2 *)(puVar7 + 0x1fe) = 0x600;
    *(undefined4 *)(puVar7 + 0x1f8) = 0x1000000;
    bVar4 = param_3 << 1 | 1;
    *(uint *)(puVar7 + 500) = uVar5 >> 0x10 | uVar5 << 0x10;
    if (DAT_ffff00009090a800 < 2) {
      DAT_ffff00009090a810 = 0;
      if (param_3 != '\x04') {
        DAT_ffff00009090a810 = bVar4;
      }
      DAT_ffff00009090a801 = '\x10';
      DAT_ffff00009090a81c = 0;
      DAT_ffff00009090a814 = 0;
    }
    else {
      lVar1 = (ulong)param_7 * 0x10;
      bVar3 = 0;
      if (param_3 != '\x04') {
        bVar3 = bVar4;
      }
      uVar5 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
      uVar6 = (param_5 & 0xff00ff00) >> 8 | (param_5 & 0xff00ff) << 8;
      DAT_ffff00009090a801 = param_7 * '\x10' + '\x10';
      (&DAT_ffff00009090a810)[lVar1] = bVar3;
      *(uint *)((long)&DAT_ffff00009090a814 + lVar1 + 4) = uVar5 >> 0x10 | uVar5 << 0x10;
      (&DAT_ffff00009090a81c)[(ulong)param_7 * 4] = uVar6 >> 0x10 | uVar6 << 0x10;
    }
    FUN_ffff00009077e11c
              (s___s___Write_swpcb_ffff0000907d3a83,
               s_ufshcd_swp_conf_block_write_prep_ffff0000907f3a94);
    FUN_ffff000090796f44(&DAT_ffff00009090a800);
    FUN_ffff00009077eb20(puVar7 + 0xe4,&DAT_ffff00009090a800,0x100);
    iVar9 = FUN_ffff000090796dac(puVar7);
    if (iVar9 == 0) {
      FUN_ffff00009077eb20(puVar7 + 0xc4,DAT_ffff00009090a588,0x20);
    }
    else {
      FUN_ffff00009077e11c
                (s___s___rpmb_get_mac_fail__ret__d_ffff000090801978,
                 s_ufshcd_swp_conf_block_write_prep_ffff0000907f3a94,iVar9);
    }
  }
  iVar9 = FUN_ffff00009079101c(uVar10,&local_90);
  uVar8 = local_44;
  if (iVar9 == 0) {
    FUN_ffff00009077ee50(lVar2,0,0x10);
    puVar7 = local_68;
    local_90 = CONCAT44(uVar8,0xc4);
    local_88 = 1;
    local_72 = 0xc;
    local_70 = CONCAT44(local_70._4_4_,0x200);
    local_84 = 0xecb500;
    uStack_80 = 1;
    uStack_7b = 2;
    if (local_68 == (undefined *)0x0) {
      FUN_ffff00009077e11c
                (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd515,
                 s_ufshcd_authen_result_read_req_pr_ffff0000907f74e4);
    }
    else {
      FUN_ffff00009077ee50(local_68,0,0x200);
      *(undefined2 *)(puVar7 + 0x1fe) = 0x500;
    }
    iVar9 = FUN_ffff00009079101c(uVar10,&local_90);
    uVar8 = local_44;
    if (iVar9 == 0) {
      FUN_ffff00009077ee50(lVar2,0,0x10);
      local_70 = CONCAT44(local_70._4_4_,0x200);
      local_90 = CONCAT44(uVar8,0xc4);
      local_88 = 2;
      uStack_7b = 2;
      local_72 = 0xc;
      local_84 = 0xeca200;
      uStack_80 = 1;
      iVar9 = FUN_ffff00009079101c(uVar10,&local_90);
      if (iVar9 == 0) {
        iVar9 = FUN_ffff0000907969a8(&local_90,param_2,0x600);
        if (iVar9 == 0) goto LAB_ffff000090795be0;
        pcVar12 = s___s___RPMB_WriteCounter_Read_Res_ffff0000907ea2bf;
      }
      else {
        pcVar12 = s___s___Sending_request_get_result_ffff0000907f3a53;
      }
    }
    else {
      pcVar12 = s___s___Sending_result_register_re_ffff0000907f0900;
    }
  }
  else {
    pcVar12 = s___s___Sending_Secure_Write_Prote_ffff0000907e8878;
  }
  FUN_ffff00009077e11c(pcVar12,s_rpmb_authen_secure_write_protect_ffff0000907f57f8,iVar9);
LAB_ffff000090795be0:
  FUN_ffff000090790038(uVar10,local_44);
  return iVar9;
}


