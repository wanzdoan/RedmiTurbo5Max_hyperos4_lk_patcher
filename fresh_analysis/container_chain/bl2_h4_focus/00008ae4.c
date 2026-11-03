// File offset entry: 0x8ae4

undefined8 FUN_ffff0000b8008ae4(long param_1,ulong param_2,ulong param_3)

{
  long lVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  uint local_14;
  
  if ((param_2 < *(ulong *)(param_1 + 0x18)) ||
     ((*(ulong *)(param_1 + 0x18) + *(long *)(param_1 + 0x20)) - 1 < param_2)) {
    return 0xffffffdb;
  }
  if ((param_2 & 0xfff) != 0) {
    return 0xfffffff8;
  }
  param_3 = param_3 & 0xffffffff;
  local_14 = 0;
  iVar2 = FUN_ffff0000b80080c0(param_1,param_2,0,&local_14);
  if ((iVar2 == 0) && ((local_14 & 0xb) == 0)) {
    FUN_ffff0000b800758c(param_2,param_3 << 0xc);
  }
  lVar1 = param_3 * 0x1000;
  lVar3 = *(long *)(param_1 + 8);
  if ((*(byte *)(param_1 + 0x10) & 1) == 0) {
    if (param_2 <= param_3 * -0x1000 + 0x1000000000000) {
      if (lVar3 != 0) {
        FUN_ffff0000b8008630(param_2,param_2,lVar1,0x27,0xc,lVar3,0);
        DataSynchronizationBarrier(3,3,0);
        return 0;
      }
LAB_ffff0000b8008c3c:
      FUN_ffff0000b804c33c
                (s__s__d__page_table_is_NULL_ffff0000b8097ccc,s_arm64_mmu_unmap_ffff0000b8095f9f,
                 0x203);
      return 0xfffffff8;
    }
    uVar4 = 0;
  }
  else {
    if (param_2 + 0x1000000000000 <= param_3 * -0x1000 + 0x1000000000000) {
      if (lVar3 != 0) {
        FUN_ffff0000b8008630(param_2,param_2 + 0x1000000000000,lVar1,0x27,0xc,lVar3,0xffffffff);
        DataSynchronizationBarrier(3,3,0);
        return 0;
      }
      goto LAB_ffff0000b8008c3c;
    }
    uVar4 = 0xffff000000000000;
  }
  FUN_ffff0000b804c33c
            (s__s__d__vaddr_0x_lx__size_0x_lx_o_ffff0000b808c2e1,s_arm64_mmu_unmap_ffff0000b8095f9f,
             0x1fe,param_2,lVar1,uVar4,0x1000000000000);
  return 0xfffffff8;
}


