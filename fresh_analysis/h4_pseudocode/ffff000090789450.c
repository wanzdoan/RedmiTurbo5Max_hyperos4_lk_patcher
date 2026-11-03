
undefined8 FUN_ffff000090789450(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  undefined1 auStack_230 [4];
  undefined1 auStack_22c [508];
  
  FUN_ffff00009077ee50(auStack_230,0,0x200);
  iVar1 = FUN_ffff000090789504(param_1,param_2,auStack_230);
  if (iVar1 < 1) {
    FUN_ffff00009077e11c(s_RSA_padding_check_PKCS1_type_1_f_ffff0000907e6d9a,iVar1);
    uVar2 = 0xfffffffe;
  }
  else {
    uVar2 = FUN_ffff00009077f0ac(&DAT_ffff000090887398,auStack_230);
    if (((int)uVar2 != 0) &&
       (uVar2 = FUN_ffff00009077f0ac(&DAT_ffff000090887398,auStack_22c), (int)uVar2 != 0)) {
      FUN_ffff00009077e11c(s_token_sig_not_same_ffff0000907e2049);
      uVar2 = 0xfffffffd;
    }
  }
  return uVar2;
}


