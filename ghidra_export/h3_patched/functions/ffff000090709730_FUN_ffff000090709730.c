// ffff000090709730  size=256  FUN_ffff000090709730

void FUN_ffff000090709730(void)

{
  undefined8 *puVar1;
  undefined8 *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  undefined4 uVar5;
  undefined8 uVar6;
  
  puVar1 = (undefined8 *)PTR_PTR_ffff000090839890;
  puVar3 = PTR_LAB_ffff000090839888;
  puVar4 = PTR_PTR_ffff000090839890;
  if (PTR_PTR_ffff000090839890 != PTR_LAB_ffff000090839888) {
    do {
      if (*(code **)(puVar4 + 8) != (code *)0x0) {
        (**(code **)(puVar4 + 8))(puVar4);
      }
      puVar4 = puVar4 + 0x28;
      puVar2 = (undefined8 *)PTR_PTR_ffff000090839890;
    } while (puVar4 != puVar3);
    while (puVar1 != (undefined8 *)puVar3) {
      if ((puVar2[2] != 0) && ((*(uint *)(puVar2 + 3) & 1) == 0)) {
        if ((*(uint *)(puVar2 + 3) >> 1 & 1) == 0) {
          uVar5 = 0x2000;
        }
        else {
          uVar5 = *(undefined4 *)(puVar2 + 4);
        }
        uVar6 = *puVar2;
        FUN_ffff00009077de54(s_starting_app__s_ffff0000907faec4,uVar6);
        uVar6 = FUN_ffff0000907178a0(uVar6,FUN_ffff000090709830,puVar2,0x10,uVar5);
        FUN_ffff000090717ac4();
        FUN_ffff0000907178d0(uVar6);
      }
      puVar1 = puVar2 + 5;
      puVar2 = puVar1;
    }
  }
  return;
}


