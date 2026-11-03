// File offset entry: 0x91e5c

void thunk_FUN_02091c94(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  iVar1 = param_1 * 0xd;
  if (0x13b13b13 < param_1) {
    iVar1 = -1;
  }
  uVar2 = func_0x02091b28();
  do {
    uVar3 = func_0x02091c50(uVar2,iVar1);
  } while ((uVar3 & 1) == 0);
  return;
}


