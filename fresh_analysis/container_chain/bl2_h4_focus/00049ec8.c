// File offset entry: 0x49ec8

undefined8 FUN_ffff0000b8049ec8(ulong param_1)

{
  long lVar1;
  int iVar2;
  undefined8 *puVar3;
  undefined8 local_18;
  
  if (param_1 < 0xffff000000000000) {
    if (param_1 - 0x1000000 < 0xfffffe000000) {
      lVar1 = tpidr_el1;
      puVar3 = *(undefined8 **)(lVar1 + 0x38);
      if (puVar3 != (undefined8 *)0x0) goto LAB_ffff0000b8049f18;
    }
    local_18 = 0;
  }
  else {
    puVar3 = &DAT_ffff0000b8129bd0;
LAB_ffff0000b8049f18:
    local_18 = 0;
    iVar2 = FUN_ffff0000b80080c0(puVar3 + 0xb,param_1,&local_18,0);
    if (iVar2 != 0) {
      local_18 = 0;
    }
  }
  return local_18;
}


