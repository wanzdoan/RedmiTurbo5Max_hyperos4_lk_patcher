
ulong FUN_ffff0000be08a118(long param_1,int param_2)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  
  FUN_ffff0000be05fd98(s___s__SW__Hash_verify_ffff0000be0ba88f,&DAT_ffff0000be0bb811);
  if (param_1 == 0) {
    uVar2 = 0x100000;
  }
  else {
    iVar1 = DAT_ffff0000be1b6ac4 * 0x100 + 0x20c;
    if (2 < DAT_ffff0000be1b6ac4) {
      iVar1 = 0;
    }
    if (iVar1 == param_2) {
      uVar2 = FUN_ffff0000be07f084(param_1,param_2,&DAT_ffff0000be1b7384,DAT_ffff0000be1b6ac4);
      if ((int)uVar2 == 0) {
        iVar1 = FUN_ffff0000be08748c(&DAT_ffff0000be1b7384,&DAT_ffff0000be1b73c4,0x20);
        uVar3 = 0;
        if (iVar1 != 0) {
          uVar3 = 0x7019;
        }
        uVar2 = (ulong)uVar3;
      }
    }
    else {
      FUN_ffff0000be05fd98
                (s_sec_level___d__buf_sz___d__key_s_ffff0000be0bca38,DAT_ffff0000be1b6ac4,param_2);
      uVar2 = 0x101008;
    }
  }
  return uVar2;
}


