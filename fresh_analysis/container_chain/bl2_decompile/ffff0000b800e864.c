
void FUN_ffff0000b800e864(int *param_1,uint param_2)

{
  ulong uVar1;
  uint uVar2;
  int iVar3;
  
  uVar1 = (ulong)param_2;
  if ((&DAT_ffff0000b811ae64)[uVar1] == '\0') {
    (&DAT_ffff0000b811ae64)[uVar1] = 1;
    *(undefined8 *)((long)&DAT_ffff0000b811ae68 + uVar1 * 0xc) = 0x84d000000000;
    (&DAT_ffff0000b811ae70)[uVar1 * 3] = 500;
  }
  if (param_2 != 2) {
    FUN_ffff0000b800ee38(param_1);
    return;
  }
  if (DAT_ffff0000b811ae60 == 0) {
    iVar3 = 0;
  }
  else {
    iVar3 = 0;
    uVar2 = 0;
    do {
      FUN_ffff0000b800ee38(param_1,uVar2);
      uVar2 = uVar2 + 1;
      iVar3 = *param_1 + iVar3;
    } while (uVar2 < DAT_ffff0000b811ae60);
  }
  *param_1 = iVar3;
  return;
}


