// ffff00009072e180  size=108  FUN_ffff00009072e180

ulong FUN_ffff00009072e180(void)

{
  int iVar1;
  ulong uVar2;
  
  uVar2 = FUN_ffff000090797bbc();
  if ((int)uVar2 != 0) {
    iVar1 = FUN_ffff00009077ede4(s_md_disable_ffff0000907d43c9,s_md_disable_ffff0000907d43c9);
    if (iVar1 == 0) {
      uVar2 = (ulong)((DAT_ffff00009086d718 ^ 0xffffffff) & 1);
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___CCCI_args__s_not_support_ffff0000907dc589,
                 s_get_args_setting_ffff0000907f4640,0x65,s_md_disable_ffff0000907d43c9);
      uVar2 = 1;
    }
  }
  return uVar2;
}


