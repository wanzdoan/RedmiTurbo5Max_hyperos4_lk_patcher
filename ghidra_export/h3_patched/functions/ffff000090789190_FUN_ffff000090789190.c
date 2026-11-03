// ffff000090789190  size=180  FUN_ffff000090789190

undefined8 FUN_ffff000090789190(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  undefined1 auStack_230 [4];
  undefined1 auStack_22c [508];
  
  FUN_ffff00009077eb88(auStack_230,0,0x200);
  iVar1 = FUN_ffff000090789244(param_1,param_2,auStack_230);
  if (iVar1 < 1) {
    FUN_ffff00009077de54(s_RSA_padding_check_PKCS1_type_1_f_ffff0000907e6c3f,iVar1);
    uVar2 = 0xfffffffe;
  }
  else {
    uVar2 = FUN_ffff00009077ede4(&DAT_ffff000090887300,auStack_230);
    if (((int)uVar2 != 0) &&
       (uVar2 = FUN_ffff00009077ede4(&DAT_ffff000090887300,auStack_22c), (int)uVar2 != 0)) {
      FUN_ffff00009077de54(s_token_sig_not_same_ffff0000907e1f61);
      uVar2 = 0xfffffffd;
    }
  }
  return uVar2;
}


