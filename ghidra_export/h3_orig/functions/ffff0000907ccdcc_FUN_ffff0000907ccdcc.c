// ffff0000907ccdcc  size=280  FUN_ffff0000907ccdcc

int FUN_ffff0000907ccdcc
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  int iVar1;
  undefined1 auStack_198 [272];
  undefined1 auStack_88 [24];
  undefined1 auStack_70 [24];
  undefined1 auStack_58 [24];
  
  FUN_ffff00009077eb88(auStack_198,0,0x158);
  FUN_ffff0000907c9f30(auStack_198);
  FUN_ffff0000907cc628(auStack_198,3);
  FUN_ffff0000907c70d0(auStack_88,&DAT_ffff000090838a5c,0x20);
  FUN_ffff0000907c70d0(auStack_70,&DAT_ffff000090838a7c,0x20);
  FUN_ffff0000907c6eb4(auStack_58,1);
  FUN_ffff00009077de54(s__s_IN_ffff0000907fe191,s_ecdsa_verify_sig_ffff0000907dbd0f);
  iVar1 = FUN_ffff0000907c68b4(auStack_198,param_3,param_4,param_1,param_2);
  if (iVar1 == 0) {
    FUN_ffff00009077de54(s_ECDSA_Signature_Verification_Suc_ffff0000907e716e);
    FUN_ffff00009077de54(s__s_OUT_ffff0000907d85e8,s_ecdsa_verify_sig_ffff0000907dbd0f);
  }
  else {
    FUN_ffff00009077de54
              (s__s_ECDSA_Signature_Verification_F_ffff0000907da38d,
               s_ecdsa_verify_sig_ffff0000907dbd0f,iVar1);
  }
  return iVar1;
}


