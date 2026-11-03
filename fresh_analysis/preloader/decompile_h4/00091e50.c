// File offset entry: 0x91e50

void thunk_FUN_02091ce4(uint param_1)

{
  undefined4 uVar1;
  ulong uVar2;
  int iVar3;
  
  if (param_1 < 0x50a8e) {
    iVar3 = (param_1 * 0x208 + 999) / 1000 + param_1 * 13000;
  }
  else {
    iVar3 = -1;
  }
  uVar1 = func_0x02091b28();
  do {
    uVar2 = func_0x02091c50(uVar1,iVar3);
  } while ((uVar2 & 1) == 0);
  return;
}


