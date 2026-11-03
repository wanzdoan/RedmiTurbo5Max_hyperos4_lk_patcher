
ulong FUN_ffff0000b806c71c(long param_1,int param_2)

{
  int iVar1;
  ulong uVar2;
  uint uVar3;
  
  FUN_ffff0000b804c33c(s___s__SW__Hash_verify_ffff0000b8097b9f,&DAT_ffff0000b80987e6);
  if (param_1 == 0) {
    uVar2 = 0x100000;
  }
  else {
    iVar1 = DAT_ffff0000b81b8804 * 0x100 + 0x20c;
    if (2 < DAT_ffff0000b81b8804) {
      iVar1 = 0;
    }
    if (iVar1 == param_2) {
      uVar2 = FUN_ffff0000b80616c4(param_1,param_2,&DAT_ffff0000b81b90c4,DAT_ffff0000b81b8804);
      if ((int)uVar2 == 0) {
        iVar1 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b90c4,&DAT_ffff0000b81b9104,0x20);
        uVar3 = 0;
        if (iVar1 != 0) {
          uVar3 = 0x7019;
        }
        uVar2 = (ulong)uVar3;
      }
    }
    else {
      FUN_ffff0000b804c33c
                (s_sec_level___d__buf_sz___d__key_s_ffff0000b80996c0,DAT_ffff0000b81b8804,param_2);
      uVar2 = 0x101008;
    }
  }
  return uVar2;
}


