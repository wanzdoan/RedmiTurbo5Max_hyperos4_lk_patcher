// ffff0000907afc5c  size=204  FUN_ffff0000907afc5c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907afc5c(long param_1,int param_2)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  
  FUN_ffff00009077de54(s___s__SW__Hash_verify_ffff0000907f08ed,&DAT_ffff0000907f20e7);
  if (param_1 == 0) {
    uVar2 = 0x100000;
  }
  else {
    iVar1 = _DAT_ffff00009090c50c * 0x100 + 0x20c;
    if (2 < _DAT_ffff00009090c50c) {
      iVar1 = 0;
    }
    if (iVar1 == param_2) {
      uVar2 = FUN_ffff0000907a26fc(param_1,param_2,0xffff00009090ce04,_DAT_ffff00009090c50c);
      if ((int)uVar2 == 0) {
        iVar1 = FUN_ffff0000907acfd8(0xffff00009090ce04,0xffff00009090ce44,0x20);
        uVar3 = 0;
        if (iVar1 != 0) {
          uVar3 = 0x7019;
        }
        uVar2 = (ulong)uVar3;
      }
    }
    else {
      FUN_ffff00009077de54
                (s_sec_level___d__buf_sz___d__key_s_ffff0000907f3a63,_DAT_ffff00009090c50c,param_2);
      uVar2 = 0x101008;
    }
  }
  return uVar2;
}


