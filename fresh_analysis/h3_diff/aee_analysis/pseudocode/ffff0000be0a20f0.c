
int FUN_ffff0000be0a20f0
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  int iVar1;
  undefined1 auStack_198 [272];
  undefined1 auStack_88 [24];
  undefined1 auStack_70 [24];
  undefined1 auStack_58 [24];
  
  FUN_ffff0000be060ae0(auStack_198,0,0x158);
  FUN_ffff0000be09f254(auStack_198);
  FUN_ffff0000be0a194c(auStack_198,3);
  FUN_ffff0000be09c3f4(auStack_88,&DAT_ffff0000be0f4bb4,0x20);
  FUN_ffff0000be09c3f4(auStack_70,&DAT_ffff0000be0f4bd4,0x20);
  FUN_ffff0000be09c1d8(auStack_58,1);
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0c36ba,s_ecdsa_verify_sig_ffff0000be0ac2d5);
  iVar1 = FUN_ffff0000be09bbd8(auStack_198,param_3,param_4,param_1,param_2);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98(s_ECDSA_Signature_Verification_Suc_ffff0000be0b3a0d);
    FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0a9dc1,s_ecdsa_verify_sig_ffff0000be0ac2d5);
  }
  else {
    FUN_ffff0000be05fd98
              (s__s_ECDSA_Signature_Verification_F_ffff0000be0ab0ec,
               s_ecdsa_verify_sig_ffff0000be0ac2d5,iVar1);
  }
  return iVar1;
}


