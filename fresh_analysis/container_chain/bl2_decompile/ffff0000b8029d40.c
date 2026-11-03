
int FUN_ffff0000b8029d40(uint *param_1)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar2 = DAT_ffff0000b8124fb0 - DAT_ffff0000b8124fa8;
  FUN_ffff0000b804c33c
            (s__s__d___VCP__vcp_reg_base_phy_ad_ffff0000b808735f,s_vcp_do_regdump_ffff0000b809820a,
             0xa4,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff0000b804c33c
              (s__VCP__error___s_reg_size_0x_x___m_ffff0000b80a050b,
               s_vcp_do_regdump_ffff0000b809820a,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff0000b804d070(param_1,0,iVar2);
    uVar3 = 0;
    DAT_ffff0000b80ea460 = DAT_ffff0000b80ea460 + 0x31800000;
    DAT_ffff0000b80ea468 = DAT_ffff0000b80ea468 + 0x31800000;
    DAT_ffff0000b80ea470 = DAT_ffff0000b80ea470 + 0x31800000;
    DAT_ffff0000b80ea480 = DAT_ffff0000b80ea480 + 0x31800000;
    DAT_ffff0000b80ea478 = DAT_ffff0000b80ea478 + 0x31800000;
    DAT_ffff0000b80ea488 = DAT_ffff0000b80ea488 + 0x31800000;
    DAT_ffff0000b80ea490 = DAT_ffff0000b80ea490 + 0x31800000;
    DAT_ffff0000b80ea498 = DAT_ffff0000b80ea498 + 0x31800000;
    DAT_ffff0000b80ea4a0 = DAT_ffff0000b80ea4a0 + 0x31800000;
    DAT_ffff0000b80ea4a8 = DAT_ffff0000b80ea4a8 + 0x31800000;
    DAT_ffff0000b80ea4b0 = DAT_ffff0000b80ea4b0 + 0x31800000;
    puVar4 = &DAT_ffff0000b80ea464;
    do {
      FUN_ffff0000b804c33c
                (s__s__d___VCP__vcp_reg_save_list___ffff0000b80882df,
                 s_vcp_do_regdump_ffff0000b809820a,0xb1,uVar3 & 0xffffffff,puVar4[-1]);
      uVar1 = puVar4[-1];
      *param_1 = uVar1;
      param_1[1] = *puVar4;
      FUN_ffff0000b804cd40(param_1 + 2,(ulong)uVar1 & 0xfffffff | 0xffff000030000000);
      uVar3 = uVar3 + 1;
      param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar4 & 0xfffffffc));
      puVar4 = puVar4 + 2;
    } while (uVar3 != 0xb);
  }
  return iVar2;
}


