
void FUN_ffff0000b803af0c(uint param_1)

{
  uint uVar1;
  long lVar2;
  undefined4 *puVar3;
  long lVar4;
  undefined1 auStack_440 [512];
  undefined1 auStack_240 [512];
  
  FUN_ffff0000b804d070(auStack_240,0,0x200);
  FUN_ffff0000b804d070(auStack_440,0,0x200);
  lVar4 = 0;
  puVar3 = &DAT_ffff0000b80eec4c + (ulong)param_1 * 0x4b;
  do {
    if (puVar3[lVar4] == -1) {
      if (lVar4 == 0) {
        return;
      }
      break;
    }
    lVar4 = lVar4 + 1;
  } while (lVar4 != 0x4b);
  do {
    uVar1 = FUN_ffff0000b804d6a4(auStack_440,&DAT_ffff0000b80a0e76,*puVar3);
    if (-1 < (int)uVar1) {
      lVar2 = FUN_ffff0000b804d3cc(auStack_240);
      if (lVar2 + (ulong)uVar1 < 0x200) {
        FUN_ffff0000b804d3ec(auStack_240,auStack_440,(ulong)uVar1);
      }
    }
    FUN_ffff0000b804d070(auStack_440,0,0x200);
    lVar4 = lVar4 + -1;
    puVar3 = puVar3 + 1;
  } while (lVar4 != 0);
  return;
}


