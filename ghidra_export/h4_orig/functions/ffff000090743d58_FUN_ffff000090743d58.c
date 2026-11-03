// ffff000090743d58  size=440  FUN_ffff000090743d58

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090743d58(uint *param_1)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar2 = _DAT_ffff000090877518 - _DAT_ffff000090877510;
  FUN_ffff00009077e11c
            (s__s__d___VCP__vcp_reg_base_phy_ad_ffff0000907d2f1f,s_vcp_do_regdump_ffff0000907f1741,
             0xa4,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff00009077e11c
              (s__VCP__error___s_reg_size_0x_x___m_ffff000090800dfd,
               s_vcp_do_regdump_ffff0000907f1741,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff00009077ee50(param_1,0,iVar2);
    uVar3 = 0;
    DAT_ffff00009081a658 = DAT_ffff00009081a658 + 0x31800000;
    DAT_ffff00009081a660 = DAT_ffff00009081a660 + 0x31800000;
    DAT_ffff00009081a668 = DAT_ffff00009081a668 + 0x31800000;
    DAT_ffff00009081a678 = DAT_ffff00009081a678 + 0x31800000;
    DAT_ffff00009081a670 = DAT_ffff00009081a670 + 0x31800000;
    DAT_ffff00009081a680 = DAT_ffff00009081a680 + 0x31800000;
    DAT_ffff00009081a688 = DAT_ffff00009081a688 + 0x31800000;
    DAT_ffff00009081a690 = DAT_ffff00009081a690 + 0x31800000;
    DAT_ffff00009081a698 = DAT_ffff00009081a698 + 0x31800000;
    DAT_ffff00009081a6a0 = DAT_ffff00009081a6a0 + 0x31800000;
    DAT_ffff00009081a6a8 = DAT_ffff00009081a6a8 + 0x31800000;
    puVar4 = &DAT_ffff00009081a65c;
    do {
      FUN_ffff00009077e11c
                (s__s__d___VCP__vcp_reg_save_list___ffff0000907d4887,
                 s_vcp_do_regdump_ffff0000907f1741,0xb1,uVar3 & 0xffffffff,puVar4[-1]);
      uVar1 = puVar4[-1];
      *param_1 = uVar1;
      param_1[1] = *puVar4;
      FUN_ffff00009077eb20(param_1 + 2,(ulong)uVar1 & 0xfffffff | 0xffff000030000000);
      uVar3 = uVar3 + 1;
      param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar4 & 0xfffffffc));
      puVar4 = puVar4 + 2;
    } while (uVar3 != 0xb);
  }
  return iVar2;
}


