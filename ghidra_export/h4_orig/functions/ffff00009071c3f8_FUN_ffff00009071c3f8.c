// ffff00009071c3f8  size=160  FUN_ffff00009071c3f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071c3f8(void)

{
  undefined *puVar1;
  uint *puVar2;
  ulong uVar3;
  ulong uVar4;
  uint *puVar5;
  
  puVar1 = PTR_PTR_ffff000090839940;
  if (PTR_DAT_ffff000090839948 != PTR_PTR_ffff000090839940) {
    uVar3 = (ulong)_DAT_ffff000088600000;
    puVar5 = (uint *)PTR_DAT_ffff000090839948;
    do {
      if (uVar3 != 0) {
        puVar2 = (uint *)&DAT_ffff000088600000;
        uVar4 = uVar3;
        do {
          if (*puVar5 == puVar2[1]) {
            (**(code **)(puVar5 + 2))();
            uVar3 = (ulong)_DAT_ffff000088600000;
            break;
          }
          puVar2 = (uint *)((long)puVar2 + uVar4);
          uVar4 = (ulong)*puVar2;
        } while (*puVar2 != 0);
      }
      puVar5 = puVar5 + 6;
    } while (puVar5 != (uint *)puVar1);
  }
  return;
}


