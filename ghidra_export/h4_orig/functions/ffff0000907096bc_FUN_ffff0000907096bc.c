// ffff0000907096bc  size=256  FUN_ffff0000907096bc

void FUN_ffff0000907096bc(void)

{
  undefined8 *puVar1;
  undefined8 *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  undefined4 uVar5;
  undefined8 uVar6;
  
  puVar1 = (undefined8 *)PTR_PTR_ffff0000908398b0;
  puVar3 = PTR_LAB_ffff0000908398a8;
  puVar4 = PTR_PTR_ffff0000908398b0;
  if (PTR_PTR_ffff0000908398b0 != PTR_LAB_ffff0000908398a8) {
    do {
      if (*(code **)(puVar4 + 8) != (code *)0x0) {
        (**(code **)(puVar4 + 8))(puVar4);
      }
      puVar4 = puVar4 + 0x28;
      puVar2 = (undefined8 *)PTR_PTR_ffff0000908398b0;
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
        FUN_ffff00009077e11c(s_starting_app__s_ffff0000907fb060,uVar6);
        uVar6 = FUN_ffff0000907179e4(uVar6,FUN_ffff0000907097bc,puVar2,0x10,uVar5);
        FUN_ffff000090717c08();
        FUN_ffff000090717a14(uVar6);
      }
      puVar1 = puVar2 + 5;
      puVar2 = puVar1;
    }
  }
  return;
}


