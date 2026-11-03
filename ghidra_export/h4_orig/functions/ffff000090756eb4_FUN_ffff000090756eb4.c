// ffff000090756eb4  size=56  FUN_ffff000090756eb4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756eb4(int param_1)

{
  undefined1 *puVar1;
  undefined1 *puVar2;
  undefined8 uVar3;
  
  if (param_1 != 0x21) {
    puVar1 = (undefined1 *)0xffff0000320900a0;
    if (param_1 == 0x13) {
      puVar1 = &DAT_ffff0000320a00a0;
    }
    puVar2 = (undefined1 *)0xffff0000320c00a0;
    if (param_1 != 0x20) {
      puVar2 = puVar1;
    }
    uVar3 = FUN_ffff000090756eec(puVar2);
    return uVar3;
  }
  _DAT_ffff0000320b00a0 = _DAT_ffff0000320b00a0 | 5;
  return 0;
}


