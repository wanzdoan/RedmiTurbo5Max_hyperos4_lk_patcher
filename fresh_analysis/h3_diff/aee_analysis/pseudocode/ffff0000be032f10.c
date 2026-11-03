
int FUN_ffff0000be032f10(uint *param_1)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar2 = DAT_ffff0000be121798 - DAT_ffff0000be121790;
  FUN_ffff0000be05fd98
            (s__s__d___VCP__vcp_reg_base_phy_ad_ffff0000be0a5cce,s_vcp_do_regdump_ffff0000be0bb064,
             0xa4,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff0000be05fd98
              (s__VCP__error___s_reg_size_0x_x___m_ffff0000be0c5339,
               s_vcp_do_regdump_ffff0000be0bb064,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff0000be060ae0(param_1,0,iVar2);
    uVar3 = 0;
    DAT_ffff0000be0d7530 = DAT_ffff0000be0d7530 + 0x31800000;
    DAT_ffff0000be0d7538 = DAT_ffff0000be0d7538 + 0x31800000;
    DAT_ffff0000be0d7540 = DAT_ffff0000be0d7540 + 0x31800000;
    DAT_ffff0000be0d7550 = DAT_ffff0000be0d7550 + 0x31800000;
    DAT_ffff0000be0d7548 = DAT_ffff0000be0d7548 + 0x31800000;
    DAT_ffff0000be0d7558 = DAT_ffff0000be0d7558 + 0x31800000;
    DAT_ffff0000be0d7560 = DAT_ffff0000be0d7560 + 0x31800000;
    DAT_ffff0000be0d7568 = DAT_ffff0000be0d7568 + 0x31800000;
    DAT_ffff0000be0d7570 = DAT_ffff0000be0d7570 + 0x31800000;
    DAT_ffff0000be0d7578 = DAT_ffff0000be0d7578 + 0x31800000;
    DAT_ffff0000be0d7580 = DAT_ffff0000be0d7580 + 0x31800000;
    puVar4 = &DAT_ffff0000be0d7534;
    do {
      FUN_ffff0000be05fd98
                (s__s__d___VCP__vcp_reg_save_list___ffff0000be0a6faf,
                 s_vcp_do_regdump_ffff0000be0bb064,0xb1,uVar3 & 0xffffffff,puVar4[-1]);
      uVar1 = puVar4[-1];
      *param_1 = uVar1;
      param_1[1] = *puVar4;
      FUN_ffff0000be0607b0(param_1 + 2,(ulong)uVar1 & 0xfffffff | 0xffff000030000000);
      uVar3 = uVar3 + 1;
      param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar4 & 0xfffffffc));
      puVar4 = puVar4 + 2;
    } while (uVar3 != 0xb);
  }
  return iVar2;
}


