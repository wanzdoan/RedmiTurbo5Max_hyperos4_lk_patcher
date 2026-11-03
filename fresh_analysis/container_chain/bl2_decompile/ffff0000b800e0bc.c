
void FUN_ffff0000b800e0bc(uint *param_1)

{
  uint *puVar1;
  uint uVar2;
  undefined *puVar3;
  uint *puVar4;
  
  puVar3 = PTR_DAT_ffff0000b81086f8;
  puVar1 = (uint *)PTR_DAT_ffff0000b8108700;
  do {
    if (puVar1 == (uint *)puVar3) {
      return;
    }
    uVar2 = *param_1;
    if (uVar2 != 0) {
      puVar4 = param_1;
      do {
        if (*puVar1 == puVar4[1]) {
          FUN_ffff0000b804cd40(puVar4 + 2,*(undefined8 *)(puVar1 + 2),puVar1[1]);
          break;
        }
        puVar4 = (uint *)((long)puVar4 + (ulong)uVar2);
        uVar2 = *puVar4;
      } while (uVar2 != 0);
    }
    puVar1 = puVar1 + 6;
  } while( true );
}


