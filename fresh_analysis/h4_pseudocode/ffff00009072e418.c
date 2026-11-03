
ulong FUN_ffff00009072e418(void)

{
  int iVar1;
  ulong uVar2;
  
  uVar2 = FUN_ffff000090797e7c();
  if ((int)uVar2 != 0) {
    iVar1 = FUN_ffff00009077f0ac(s_md_disable_ffff0000907d4406,s_md_disable_ffff0000907d4406);
    if (iVar1 == 0) {
      uVar2 = (ulong)((DAT_ffff00009086d7b0 ^ 0xffffffff) & 1);
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___CCCI_args__s_not_support_ffff0000907dc676,
                 s_get_args_setting_ffff0000907f47c4,0x65,s_md_disable_ffff0000907d4406);
      uVar2 = 1;
    }
  }
  return uVar2;
}


