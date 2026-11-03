
int FUN_ffff0000be00f560(ulong param_1,uint param_2,uint param_3)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined4 uVar4;
  char *pcVar5;
  undefined8 uVar6;
  ulong uVar7;
  long lVar8;
  ulong uVar9;
  long local_48;
  
  uVar7 = (ulong)param_3;
  local_48 = 0;
  if (param_3 <= param_2) {
    uVar1 = 0;
    if (param_3 != 0) {
      uVar1 = param_2 / param_3;
    }
    param_2 = param_2 - uVar1 * param_3;
  }
  if ((param_3 < 0x100000) || ((DAT_ffff0000be0fd060 & 1) != 0)) {
    iVar2 = FUN_ffff0000be0636d8(param_2 + param_1,param_3 - param_2);
    DAT_ffff0000be0fd064 = 0;
    if (param_2 != 0) {
      iVar3 = FUN_ffff0000be0636d8(param_1,(ulong)param_2);
      iVar2 = iVar3 + iVar2;
    }
  }
  else {
    iVar2 = FUN_ffff0000be05d9a8
                      (PTR_DAT_ffff0000be0f5570,s_vm_compress_kdebug_ffff0000be0bdf98,
                       (param_1 & 0xfff) + uVar7 + 0xfff & 0x1fffff000,&local_48,0xc,
                       param_1 & 0xfffffffffffff000,0,0);
    if ((iVar2 == 0) && (lVar8 = local_48 + (param_1 & 0xfff), lVar8 != 0)) {
      pcVar5 = (char *)FUN_ffff0000be05f1bc(0x100000);
      if (pcVar5 != (char *)0x0) {
        iVar2 = 0;
        do {
          FUN_ffff0000be060ae0(pcVar5,0,0x100000);
          uVar9 = 0x100000;
          if ((uint)uVar7 >> 0x14 == 0) {
            FUN_ffff0000be063b40(s_kedump__kedump_misc_compression_l_ffff0000be0c3bb2,uVar7);
            FUN_ffff0000be05fd98(s_kedump__kedump_misc_compression_l_ffff0000be0c3bb2,uVar7);
            uVar9 = uVar7;
          }
          uVar4 = FUN_ffff0000be00cd5c(lVar8,pcVar5,uVar9,0x100000);
          uVar6 = FUN_ffff0000be05d228(pcVar5);
          iVar3 = FUN_ffff0000be0636d8(uVar6,uVar4);
          lVar8 = lVar8 + 0x100000;
          iVar2 = iVar3 + iVar2;
          uVar1 = (uint)uVar7 - 0x100000;
          uVar7 = (ulong)uVar1;
        } while (0 < (int)uVar1);
        if ((*pcVar5 == '\x1f') && (pcVar5[1] == -0x75)) {
          DAT_ffff0000be0fd064 = 1;
        }
        thunk_FUN_ffff0000be09937c(pcVar5);
        FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_48);
        return iVar2;
      }
      FUN_ffff0000be063b40(s_kedump__malloc_failed_for_output_ffff0000be0ac4f5);
      FUN_ffff0000be05fd98(s_kedump__malloc_failed_for_output_ffff0000be0ac4f5);
      FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_48);
    }
    else {
      FUN_ffff0000be063b40(s_kedump__vmm_alloc_physical_map_e_ffff0000be0b9761);
      FUN_ffff0000be05fd98(s_kedump__vmm_alloc_physical_map_e_ffff0000be0b9761);
    }
    iVar2 = 0;
  }
  return iVar2;
}


