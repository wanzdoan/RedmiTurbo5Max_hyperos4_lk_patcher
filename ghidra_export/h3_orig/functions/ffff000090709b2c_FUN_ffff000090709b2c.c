// ffff000090709b2c  size=92  FUN_ffff000090709b2c

undefined * FUN_ffff000090709b2c(undefined8 param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)&DAT_ffff00009083d168;
  do {
    puVar2 = (undefined8 *)*puVar2;
    if (puVar2 == (undefined8 *)0x0) {
      return &DAT_ffff0000907df295;
    }
    iVar1 = FUN_ffff00009077ede4(param_1,puVar2[1]);
  } while (iVar1 != 0);
  return (undefined *)puVar2[2];
}


