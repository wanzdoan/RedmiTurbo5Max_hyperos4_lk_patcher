
undefined8 FUN_ffff00009071c738(long *param_1,uint *param_2)

{
  undefined8 uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  undefined8 uVar7;
  uint uVar8;
  long local_50 [4];
  
  iVar4 = FUN_ffff000090781854();
  if (iVar4 == 0) {
    uVar8 = 0;
  }
  else {
    iVar4 = 0;
    uVar8 = 0;
    do {
      FUN_ffff000090781834(uVar8);
      lVar6 = FUN_ffff000090781868(uVar8,0);
      iVar2 = *(int *)(lVar6 + 0x20);
      lVar6 = FUN_ffff000090781868(uVar8,1);
      iVar3 = *(int *)(lVar6 + 0x20);
      lVar6 = FUN_ffff000090781868(uVar8,2);
      uVar8 = uVar8 + 1;
      iVar4 = *(int *)(lVar6 + 0x20) + iVar3 + iVar2 + iVar4;
      uVar5 = FUN_ffff000090781854();
    } while (uVar8 < uVar5);
    uVar8 = iVar4 + 0xffffU & 0xffff0000;
  }
  *param_2 = uVar8;
  iVar4 = FUN_ffff00009071c708();
  uVar7 = 0;
  if (iVar4 != 0) {
    uVar7 = 0x240000000;
  }
  uVar1 = 0x100000000;
  if (iVar4 != 0) {
    uVar1 = 0x280000000;
  }
  lVar6 = FUN_ffff00009071a09c
                    (uVar8,0x2000000,uVar7,uVar1,0,0,s_adsp_image_reserved_ffff0000907ef4fe);
  *param_1 = lVar6;
  if (lVar6 == 0) {
    uVar7 = 5;
  }
  else {
    local_50[2] = 0;
    local_50[3] = 0;
    local_50[0] = 0;
    local_50[1] = 0;
    FUN_ffff00009071ebf4(0xc2000419,6,0,lVar6,uVar8,0,0,0,local_50);
    if (local_50[0] != 0) {
      FUN_ffff00009077e11c
                (s__s_fail_CFG_REMAP_SYSRAM_ret__ld_ffff000090800730,&DAT_ffff0000907da79c);
    }
    uVar7 = 0;
  }
  return uVar7;
}


