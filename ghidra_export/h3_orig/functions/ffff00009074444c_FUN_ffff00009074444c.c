// ffff00009074444c  size=440  FUN_ffff00009074444c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009074444c(uint *param_1)

{
  ulong uVar1;
  int iVar2;
  
  iVar2 = _DAT_ffff0000908774c8 - _DAT_ffff0000908774c0;
  FUN_ffff00009077de54
            (s__s__d___MMUP__mmup_reg_base_phy_a_ffff0000907d942e,s_mmup_do_regdump_ffff0000907dc9d3
             ,0x7d,0x31800000,iVar2);
  if (iVar2 - 0x4001U < 0xffffc000) {
    FUN_ffff00009077de54
              (s__MMUP__error___s_reg_size_0x_x___ffff0000907d6372,
               s_mmup_do_regdump_ffff0000907dc9d3,iVar2,0x4000);
    iVar2 = 0;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,iVar2);
    DAT_ffff00009081a690 = DAT_ffff00009081a690 + 0x31800000;
    DAT_ffff00009081a694 = DAT_ffff00009081a694 + 0x31800000;
    DAT_ffff00009081a698 = DAT_ffff00009081a698 + 0x31800000;
    FUN_ffff00009077de54
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000907ff253,
               s_mmup_do_regdump_ffff0000907dc9d3,0x8a,0);
    uVar1 = (ulong)DAT_ffff00009081a690;
    *param_1 = DAT_ffff00009081a690;
    param_1[1] = 0x1e0;
    FUN_ffff00009077e858(param_1 + 2,uVar1 & 0xfffffff | 0xffff000030000000,0x1e0);
    FUN_ffff00009077de54
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000907ff253,
               s_mmup_do_regdump_ffff0000907dc9d3,0x8a,1,DAT_ffff00009081a694);
    uVar1 = (ulong)DAT_ffff00009081a694;
    param_1[0x7a] = DAT_ffff00009081a694;
    param_1[0x7b] = 0x290;
    FUN_ffff00009077e858(param_1 + 0x7c,uVar1 & 0xfffffff | 0xffff000030000000,0x290);
    FUN_ffff00009077de54
              (s__s__d___MMUP__mmup_reg_save_list_ffff0000907ff253,
               s_mmup_do_regdump_ffff0000907dc9d3,0x8a,2,DAT_ffff00009081a698);
    uVar1 = (ulong)DAT_ffff00009081a698;
    param_1[0x120] = DAT_ffff00009081a698;
    param_1[0x121] = 0x150;
    FUN_ffff00009077e858(param_1 + 0x122,uVar1 & 0xfffffff | 0xffff000030000000,0x150);
  }
  return iVar2;
}


