// ffff00009071770c  size=404  FUN_ffff00009071770c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 *
FUN_ffff00009071770c
          (undefined4 *param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
          undefined4 param_5,long param_6,undefined8 param_7)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined8 *puVar3;
  undefined4 *puVar4;
  long lVar5;
  undefined8 *puVar6;
  undefined8 *puVar7;
  uint uVar8;
  
  if (param_1 == (undefined4 *)0x0) {
    puVar4 = (undefined4 *)FUN_ffff00009077d24c(0x2d0);
    if (puVar4 == (undefined4 *)0x0) {
      return (undefined4 *)0x0;
    }
    uVar8 = 4;
  }
  else {
    uVar8 = 0;
    puVar4 = param_1;
  }
  FUN_ffff00009077eb88(puVar4,0,0x2d0);
  *puVar4 = 0x74687264;
  FUN_ffff00009077ef1c(puVar4 + 0xac,param_2,0x20);
  *(undefined8 *)(puVar4 + 0x9e) = param_3;
  *(undefined8 *)(puVar4 + 0xa0) = param_4;
  puVar4[10] = param_5;
  puVar4[0xb] = 0;
  puVar4[0x12] = 0;
  puVar4[0xa2] = 0;
  puVar4[0xa4] = 0x77616974;
  *(undefined4 **)(puVar4 + 0xa6) = puVar4 + 0xa6;
  *(undefined4 **)(puVar4 + 0xa8) = puVar4 + 0xa6;
  puVar4[0xaa] = 0;
  *(undefined8 *)(puVar4 + 0xe) = 0;
  *(undefined8 *)(puVar4 + 0x10) = 0;
  if (param_6 == 0) {
    lVar5 = FUN_ffff00009077d24c(param_7);
    *(long *)(puVar4 + 0x9a) = lVar5;
    if (lVar5 == 0) {
      if (param_1 != (undefined4 *)0x0) {
        return (undefined4 *)0x0;
      }
      thunk_FUN_ffff0000907c2ad8(puVar4);
      return (undefined4 *)0x0;
    }
    uVar8 = uVar8 | 2;
  }
  else {
    *(long *)(puVar4 + 0x9a) = param_6;
  }
  *(undefined8 *)(puVar4 + 0x9c) = param_7;
  puVar4[0xd] = uVar8;
  uVar1 = tpidr_el1;
  FUN_ffff000090708710(uVar1,puVar4);
  puVar7 = _DAT_ffff000090843688;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    puVar6 = (undefined8 *)(puVar4 + 2);
    *puVar6 = &DAT_ffff000090843680;
    puVar3 = puVar6;
    *(undefined8 **)(puVar4 + 4) = _DAT_ffff000090843688;
    _DAT_ffff000090843688 = puVar3;
    *puVar7 = puVar6;
    _DAT_ffff000090843678 = 0;
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  else {
    _DAT_ffff000090843678 = 0;
    puVar7 = (undefined8 *)(puVar4 + 2);
    *puVar7 = &DAT_ffff000090843680;
    *(undefined8 **)(puVar4 + 4) = _DAT_ffff000090843688;
    *_DAT_ffff000090843688 = puVar7;
    _DAT_ffff000090843688 = puVar7;
  }
  return puVar4;
}


