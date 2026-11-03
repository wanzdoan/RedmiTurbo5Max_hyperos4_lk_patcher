
long FUN_ffff0000be00b904(long param_1,ulong param_2,long param_3,ulong param_4,undefined8 param_5)

{
  ulong uVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  long lVar8;
  ulong uVar9;
  long local_68;
  
  uVar1 = *(ulong *)(param_1 + 0x20);
  uVar7 = (ulong)*(uint *)(param_1 + 0x28);
  local_68 = 0;
  lVar5 = (param_4 >> 0xc) * uVar7;
  uVar3 = FUN_ffff0000be010288
                    (*(long *)(param_1 + 0x18) + (param_2 >> 0xc) * uVar7 & 0xfffffffffffff000);
  iVar2 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_memmap_ffff0000be0b2ada,lVar5,&local_68,0,uVar3,0,0)
  ;
  if (iVar2 == 0) {
    if (lVar5 < 1) {
      lVar6 = 0;
    }
    else {
      lVar6 = 0;
      lVar8 = 0;
      uVar9 = 0;
      do {
        lVar4 = FUN_ffff0000be05d2b0(param_3);
        if (lVar4 == 0) {
          FUN_ffff0000be0115fc(s_get_NULL_aspace_ffff0000be0baaf8);
          goto LAB_ffff0000be00b998;
        }
        lVar4 = lVar4 + 0x58;
        FUN_ffff0000be008854(lVar4,param_3,1);
        if ((int)uVar9 == 0) {
          if ((*(ulong *)(local_68 + lVar8) & uVar1) == 0) {
            uVar9 = *(ulong *)(local_68 + lVar8) & 0x40;
            goto LAB_ffff0000be00ba40;
          }
          iVar2 = FUN_ffff0000be0085d4(lVar4,param_3,param_5,1,0);
          uVar9 = 0;
          if (iVar2 == 0) {
            lVar6 = lVar6 + 1;
          }
        }
        else {
          uVar9 = 0;
LAB_ffff0000be00ba40:
          iVar2 = FUN_ffff0000be0085d4(lVar4,param_3,param_2,1,0);
          if (iVar2 != 0) {
            FUN_ffff0000be0115fc(s_mmu_map_fail_ffff0000be0a691b);
            goto LAB_ffff0000be00b998;
          }
        }
        lVar8 = lVar8 + uVar7;
        param_3 = param_3 + 0x1000;
        param_2 = param_2 + 0x1000;
      } while (lVar8 < lVar5);
    }
    FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_68);
  }
  else {
    FUN_ffff0000be0115fc(s_vmm_alloc_physical_for_memmap_fa_ffff0000be0a8cd7,iVar2);
LAB_ffff0000be00b998:
    FUN_ffff0000be0115fc
              (s_map_error__paddr_0x_016llx__vadd_ffff0000be0ab1ce,param_2,param_3,param_5);
    lVar6 = -1;
  }
  return lVar6;
}


