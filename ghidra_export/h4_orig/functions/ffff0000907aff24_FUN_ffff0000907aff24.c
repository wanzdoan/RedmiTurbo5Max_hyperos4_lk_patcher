// ffff0000907aff24  size=204  FUN_ffff0000907aff24

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907aff24(long param_1,int param_2)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  
  FUN_ffff00009077e11c(s___s__SW__Hash_verify_ffff0000907f0a75,&DAT_ffff0000907f228a);
  if (param_1 == 0) {
    uVar2 = 0x100000;
  }
  else {
    iVar1 = _DAT_ffff00009090c5cc * 0x100 + 0x20c;
    if (2 < _DAT_ffff00009090c5cc) {
      iVar1 = 0;
    }
    if (iVar1 == param_2) {
      uVar2 = FUN_ffff0000907a29bc(param_1,param_2,0xffff00009090cec4,_DAT_ffff00009090c5cc);
      if ((int)uVar2 == 0) {
        iVar1 = FUN_ffff0000907ad298(0xffff00009090cec4,0xffff00009090cf04,0x20);
        uVar3 = 0;
        if (iVar1 != 0) {
          uVar3 = 0x7019;
        }
        uVar2 = (ulong)uVar3;
      }
    }
    else {
      FUN_ffff00009077e11c
                (s_sec_level___d__buf_sz___d__key_s_ffff0000907f3c06,_DAT_ffff00009090c5cc,param_2);
      uVar2 = 0x101008;
    }
  }
  return uVar2;
}


