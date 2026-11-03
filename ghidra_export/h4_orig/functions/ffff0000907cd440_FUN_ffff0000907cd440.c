// ffff0000907cd440  size=280  FUN_ffff0000907cd440

int FUN_ffff0000907cd440
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  int iVar1;
  undefined1 auStack_198 [272];
  undefined1 auStack_88 [24];
  undefined1 auStack_70 [24];
  undefined1 auStack_58 [24];
  
  FUN_ffff00009077ee50(auStack_198,0,0x158);
  FUN_ffff0000907ca5a4(auStack_198);
  FUN_ffff0000907ccc9c(auStack_198,3);
  FUN_ffff0000907c7744(auStack_88,&DAT_ffff000090838a7c,0x20);
  FUN_ffff0000907c7744(auStack_70,&DAT_ffff000090838a9c,0x20);
  FUN_ffff0000907c7528(auStack_58,1);
  FUN_ffff00009077e11c(s__s_IN_ffff0000907fe339,s_ecdsa_verify_sig_ffff0000907dbe1a);
  iVar1 = FUN_ffff0000907c6f28(auStack_198,param_3,param_4,param_1,param_2);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c(s_ECDSA_Signature_Verification_Suc_ffff0000907e72b2);
    FUN_ffff00009077e11c(s__s_OUT_ffff0000907d867e,s_ecdsa_verify_sig_ffff0000907dbe1a);
  }
  else {
    FUN_ffff00009077e11c
              (s__s_ECDSA_Signature_Verification_F_ffff0000907da424,
               s_ecdsa_verify_sig_ffff0000907dbe1a,iVar1);
  }
  return iVar1;
}


