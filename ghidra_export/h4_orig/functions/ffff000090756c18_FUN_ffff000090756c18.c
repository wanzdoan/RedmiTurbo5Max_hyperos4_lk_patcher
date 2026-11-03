// ffff000090756c18  size=164  FUN_ffff000090756c18

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756c18(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 uVar3;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      puVar2 = (undefined4 *)(&DAT_ffff0000320f0400 + (ulong)(param_1 == 0x12) * 0x10000);
      uVar3 = *(undefined4 *)(param_2 + 0x928);
    }
    else if (param_1 == 0x12) {
      puVar2 = (undefined4 *)&DAT_ffff000032100400;
      uVar3 = _DAT_ffff00009087b628;
    }
    else {
      if (param_1 != 0xd) {
        return 0;
      }
      puVar2 = (undefined4 *)&DAT_ffff0000320f0400;
      uVar3 = _DAT_ffff00009087b61c;
    }
    uVar1 = *(undefined4 *)(param_2 + 0x92c);
    *puVar2 = 0x80;
    puVar2[0x214] = uVar3;
    puVar2[0x215] = uVar1;
    *(undefined4 *)((ulong)puVar2 | 0x800) = 1;
  }
  return 0;
}


