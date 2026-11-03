
undefined * FUN_ffff000090709ab8(undefined8 param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = &DAT_ffff00009083d108;
  do {
    puVar2 = (undefined8 *)*puVar2;
    if (puVar2 == (undefined8 *)0x0) {
      return &DAT_ffff0000907df338;
    }
    iVar1 = FUN_ffff00009077f0ac(param_1,puVar2[1]);
  } while (iVar1 != 0);
  return (undefined *)puVar2[2];
}


