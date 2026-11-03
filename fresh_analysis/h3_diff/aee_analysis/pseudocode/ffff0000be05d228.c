
undefined8 FUN_ffff0000be05d228(ulong param_1)

{
  long lVar1;
  int iVar2;
  undefined8 *puVar3;
  undefined8 local_18;
  
  if (param_1 < 0xffff000000000000) {
    if (param_1 - 0x1000000 < 0xfffffe000000) {
      lVar1 = tpidr_el1;
      puVar3 = *(undefined8 **)(lVar1 + 0x38);
      if (puVar3 != (undefined8 *)0x0) goto LAB_ffff0000be05d278;
    }
    local_18 = 0;
  }
  else {
    puVar3 = &DAT_ffff0000be127118;
LAB_ffff0000be05d278:
    local_18 = 0;
    iVar2 = FUN_ffff0000be007e30(puVar3 + 0xb,param_1,&local_18,0);
    if (iVar2 != 0) {
      local_18 = 0;
    }
  }
  return local_18;
}


