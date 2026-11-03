// ffff00009077b06c  size=136  FUN_ffff00009077b06c

undefined8 FUN_ffff00009077b06c(ulong param_1)

{
  long lVar1;
  int iVar2;
  undefined1 *puVar3;
  undefined8 local_18;
  
  if (param_1 < 0xffff000000000000) {
    if (param_1 - 0x1000000 < 0xfffffe000000) {
      lVar1 = tpidr_el1;
      puVar3 = *(undefined1 **)(lVar1 + 0x38);
      if (puVar3 != (undefined1 *)0x0) goto LAB_ffff00009077b0bc;
    }
    local_18 = 0;
  }
  else {
    puVar3 = &DAT_ffff00009087ce60;
LAB_ffff00009077b0bc:
    local_18 = 0;
    iVar2 = FUN_ffff0000907087f8(puVar3 + 0x58,param_1,&local_18,0);
    if (iVar2 != 0) {
      local_18 = 0;
    }
  }
  return local_18;
}


