// ffff000090708784  size=576  FUN_ffff000090708784

undefined8 FUN_ffff000090708784(long param_1,ulong param_2,long *param_3,uint *param_4)

{
  ulong uVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  uint uVar5;
  uint uVar6;
  ulong uVar7;
  
  if ((param_2 < *(ulong *)(param_1 + 0x18)) ||
     ((*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1 < param_2)) {
    return 0xffffffdb;
  }
  if ((*(byte *)(param_1 + 0x10) & 1) == 0) {
    if (param_2 >> 0x30 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_arch_arm64_mmu_c_ffff0000907fcaf0,0x89,
                 s_index_<_MMU_USER_PAGE_TABLE_ENTR_ffff0000907e09f7);
    }
  }
  else {
    if (param_2 < 0xffff000000000000) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_arch_arm64_mmu_c_ffff0000907fcaf0,0x82,
                 s_index_<_MMU_KERNEL_PAGE_TABLE_EN_ffff0000907d3dc4);
    }
    param_2 = param_2 + 0x1000000000000;
  }
  uVar4 = *(ulong *)(*(long *)(param_1 + 8) + (param_2 >> 0x24 & 0xffffff8));
  uVar1 = uVar4 & 3;
  if (uVar1 == 0) {
    return 0xfffffffe;
  }
  uVar7 = param_2 & 0x7fffffffff;
  uVar2 = uVar4 & 0xfffffffff000;
  if (uVar1 != 1) {
    if (uVar1 != 3) goto LAB_ffff000090708994;
    lVar3 = FUN_ffff00009077b2cc();
    uVar4 = *(ulong *)(lVar3 + (uVar7 >> 0x1b & 0xff8));
    uVar1 = uVar4 & 3;
    if (uVar1 == 0) {
      return 0xfffffffe;
    }
    uVar2 = uVar4 & 0xfffffffff000;
    uVar7 = uVar7 - (param_2 & 0x7fc0000000);
    if (uVar1 != 1) {
      if (uVar1 != 3) goto LAB_ffff000090708994;
      lVar3 = FUN_ffff00009077b2cc();
      uVar4 = *(ulong *)(lVar3 + (uVar7 >> 0x15 & 0xffffffff) * 8);
      uVar1 = uVar4 & 3;
      if (uVar1 == 0) {
        return 0xfffffffe;
      }
      uVar2 = uVar4 & 0xfffffffff000;
      uVar7 = uVar7 + (uVar7 >> 0x15 & 0xffffffff) * -0x200000;
      if (uVar1 != 1) {
        if (uVar1 != 3) {
LAB_ffff000090708994:
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cee0
                    (s__s__d_unimplemented_ffff0000907fafd0,
                     s_status_t_arch_mmu_query_arch_asp_ffff0000907f7a87,0xa3);
        }
        lVar3 = FUN_ffff00009077b2cc();
        uVar4 = *(ulong *)(lVar3 + (uVar7 >> 0xc & 0xffffffff) * 8);
        if ((uVar4 & 3) == 0) {
          return 0xfffffffe;
        }
        if ((uVar4 & 3) != 3) goto LAB_ffff000090708994;
        uVar2 = uVar4 & 0xfffffffff000;
        uVar7 = uVar7 + (uVar7 >> 0xc & 0xffffffff) * -0x1000;
      }
    }
  }
  if (param_3 != (long *)0x0) {
    *param_3 = uVar7 + uVar2;
  }
  if (param_4 == (uint *)0x0) {
    return 0;
  }
  uVar5 = (uint)uVar4 & 0x20;
  uVar1 = uVar4 & 0x1c;
  *param_4 = uVar5;
  if (uVar1 == 0) {
    uVar6 = 1;
LAB_ffff000090708918:
    uVar5 = uVar5 | uVar6;
    *param_4 = uVar5;
  }
  else if (uVar1 != 8) {
    if (uVar1 != 4) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0
                (s__s__d_unimplemented_ffff0000907fafd0,
                 s_status_t_arch_mmu_query_arch_asp_ffff0000907f7a87,0xba);
    }
    uVar6 = 2;
    goto LAB_ffff000090708918;
  }
  uVar6 = (uint)uVar4 & 0xc0;
  if (uVar6 == 0x40) {
    uVar6 = 4;
  }
  else if (uVar6 == 0xc0) {
    uVar6 = 0xc;
  }
  else {
    if (uVar6 != 0x80) goto LAB_ffff000090708958;
    uVar6 = 8;
  }
  uVar5 = uVar5 | uVar6;
  *param_4 = uVar5;
LAB_ffff000090708958:
  if ((uVar5 >> 2 & 1) == 0) {
    uVar4 = uVar4 >> 0x35;
  }
  else {
    uVar4 = uVar4 >> 0x36;
  }
  if ((uVar4 & 1) == 0) {
    return 0;
  }
  *param_4 = uVar5 | 0x10;
  return 0;
}


