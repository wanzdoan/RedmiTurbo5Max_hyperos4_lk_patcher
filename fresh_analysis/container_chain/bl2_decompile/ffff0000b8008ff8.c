
void FUN_ffff0000b8008ff8(void)

{
  undefined8 *puVar1;
  undefined8 *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  undefined4 uVar5;
  undefined8 uVar6;
  
  puVar1 = (undefined8 *)PTR_PTR_ffff0000b81086d0;
  puVar3 = PTR_PTR_ffff0000b81086c8;
  puVar4 = PTR_PTR_ffff0000b81086d0;
  if (PTR_PTR_ffff0000b81086d0 != PTR_PTR_ffff0000b81086c8) {
    do {
      if (*(code **)(puVar4 + 8) != (code *)0x0) {
        (**(code **)(puVar4 + 8))(puVar4);
      }
      puVar4 = puVar4 + 0x28;
      puVar2 = (undefined8 *)PTR_PTR_ffff0000b81086d0;
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
        FUN_ffff0000b804c33c(s_starting_app__s_ffff0000b809d385,uVar6);
        uVar6 = FUN_ffff0000b800b03c(uVar6,FUN_ffff0000b80090f8,puVar2,0x10,uVar5);
        FUN_ffff0000b800b228();
        FUN_ffff0000b800b06c(uVar6);
      }
      puVar1 = puVar2 + 5;
      puVar2 = puVar1;
    }
  }
  return;
}


