
undefined * FUN_ffff000090709bc8(undefined8 param_1)

{
  int iVar1;
  undefined8 *puVar2;

  puVar2 = &DAT_ffff000090835168;
  do {
    puVar2 = (undefined8 *)*puVar2;
    if (puVar2 == (undefined8 *)0x0) {
      return &DAT_ffff0000907d788e;
    }
    iVar1 = FUN_ffff00009077f990(param_1,puVar2[1]);
  } while (iVar1 != 0);
  return (undefined *)puVar2[2];
}
