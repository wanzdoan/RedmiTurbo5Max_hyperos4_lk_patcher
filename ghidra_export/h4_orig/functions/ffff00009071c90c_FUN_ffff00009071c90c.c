// ffff00009071c90c  size=616  FUN_ffff00009071c90c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009071c90c(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  int iVar7;
  ulong uVar8;
  long local_70 [4];
  
  iVar1 = FUN_ffff000090781854();
  lVar6 = param_3;
  if (iVar1 != 0) {
    iVar7 = 0;
    do {
      iVar2 = FUN_ffff000090781834(iVar7);
      if (iVar2 != 0) {
        lVar4 = FUN_ffff000090781868(iVar7,2);
        uVar3 = FUN_ffff000090785d18(param_1,lVar4,lVar6,*(undefined4 *)(lVar4 + 0x20));
        if ((uVar3 == 0) || (*(uint *)(lVar4 + 0x20) < uVar3)) {
          FUN_ffff00009077e11c
                    (s__s_load_adsp_image__s_fail_ffff0000907e279c,&DAT_ffff0000907da79c,lVar4);
          return 0xfffffff0;
        }
        lVar6 = lVar6 + (ulong)*(uint *)(lVar4 + 0x20);
      }
      iVar7 = iVar7 + 1;
    } while (iVar1 != iVar7);
  }
  uVar8 = lVar6 - param_3;
  FUN_ffff00009071d0d4();
  _DAT_ffff00001a000000 = _DAT_ffff00001a000000 | 0x33;
  FUN_ffff00009077cea0(1);
  _DAT_ffff00001a345014 = _DAT_ffff00001a345014 & 0xffffff00;
  _DAT_ffff00001a000000 = _DAT_ffff00001a000000 & 0xffffffcc;
  uVar5 = uVar8;
  if (iVar1 != 0) {
    iVar7 = 0;
    do {
      iVar2 = FUN_ffff000090781834(iVar7);
      if (iVar2 != 0) {
        lVar4 = FUN_ffff000090781868(iVar7,0);
        uVar3 = FUN_ffff000090785d18(param_1,lVar4,lVar6,*(undefined4 *)(lVar4 + 0x20));
        if ((uVar3 == 0) || (*(uint *)(lVar4 + 0x20) < uVar3)) {
          return 0xffffffe0;
        }
        lVar6 = lVar6 + (ulong)*(uint *)(lVar4 + 0x20);
        lVar4 = FUN_ffff000090781868(iVar7,1);
        uVar3 = FUN_ffff000090785d18(param_1,lVar4,lVar6,*(undefined4 *)(lVar4 + 0x20));
        if (uVar3 == 0) {
          return 0xffffffe0;
        }
        if (*(uint *)(lVar4 + 0x20) < uVar3) {
          return 0xffffffe0;
        }
        lVar6 = lVar6 + (ulong)*(uint *)(lVar4 + 0x20);
      }
      iVar7 = iVar7 + 1;
    } while (iVar1 != iVar7);
    uVar5 = lVar6 - param_3;
  }
  local_70[2] = 0;
  local_70[3] = 0;
  local_70[0] = 0;
  local_70[1] = 0;
  FUN_ffff000090707c70(param_3,uVar5);
  FUN_ffff00009071ebf4(0xc2000419,4,param_2,uVar8 & 0xffffffff,0,0,0,0,local_70);
  if (local_70[0] != 0) {
    FUN_ffff00009077e11c(s__s_loader_timeout__status__ld_ffff0000907ef512,&DAT_ffff0000907da79c);
  }
  FUN_ffff00009071ebf4(0xc2000419,5,0,0,0,0,0,0,local_70);
  FUN_ffff00009071d144();
  return 0;
}


