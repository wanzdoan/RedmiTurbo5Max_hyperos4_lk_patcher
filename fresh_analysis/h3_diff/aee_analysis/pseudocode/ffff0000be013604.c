
undefined * FUN_ffff0000be013604(undefined8 param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = &DAT_ffff0000be107958;
  do {
    puVar2 = (undefined8 *)*puVar2;
    if (puVar2 == (undefined8 *)0x0) {
      return &DAT_ffff0000be0ae340;
    }
    iVar1 = FUN_ffff0000be060cb8(param_1,puVar2[1]);
  } while (iVar1 != 0);
  return (undefined *)puVar2[2];
}


