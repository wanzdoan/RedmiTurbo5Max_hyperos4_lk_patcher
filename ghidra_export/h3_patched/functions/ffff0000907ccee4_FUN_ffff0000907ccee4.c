// ffff0000907ccee4  size=308  FUN_ffff0000907ccee4

int FUN_ffff0000907ccee4(undefined8 param_1,undefined8 param_2,undefined1 *param_3)

{
  long lVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  undefined4 local_88;
  undefined1 local_84;
  undefined2 local_83;
  undefined8 local_81;
  undefined4 local_79;
  undefined4 local_75;
  undefined2 local_71;
  undefined1 local_6f;
  undefined1 local_6e;
  undefined1 local_6d;
  undefined4 local_6c;
  undefined4 uStack_68;
  undefined8 local_64;
  undefined8 local_5c;
  undefined8 local_54;
  undefined8 local_4c;
  undefined2 local_44;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_44 = 0;
  local_4c = 0;
  local_54 = 0;
  local_5c = 0;
  local_64 = 0;
  lVar3 = FUN_ffff00009076fb44(param_1,param_2,&local_40);
  if (lVar3 == 0) {
    iVar2 = 0xe;
    pcVar4 = s_hash256_fail_ffff0000907fc830;
  }
  else {
    FUN_ffff00009077de54(s_hash_success_ffff0000907f2350);
    local_83 = *(undefined2 *)(param_3 + 1);
    local_84 = *param_3;
    local_81 = *(undefined8 *)(param_3 + 3);
    local_71 = *(undefined2 *)(param_3 + 0x13);
    local_88 = 0x20024430;
    local_79 = *(undefined4 *)(param_3 + 0xb);
    local_75 = *(undefined4 *)(param_3 + 0xf);
    local_6d = param_3[0x17];
    local_6f = param_3[0x15];
    local_6e = param_3[0x16];
    local_6c = *(undefined4 *)(param_3 + 0x18);
    uStack_68 = *(undefined4 *)(param_3 + 0x1c);
    local_64 = CONCAT62(local_64._2_6_,0x2002);
    lVar3 = 0;
    do {
      lVar1 = lVar3 + 1;
      *(undefined1 *)((long)&local_64 + lVar3 + 2) = param_3[lVar3 + 0x20];
      lVar3 = lVar1;
    } while (lVar1 != 0x20);
    iVar2 = FUN_ffff0000907ccdcc(&local_88,0x46,&local_40,0x20);
    pcVar4 = s_sign_verify_success_ffff0000907fe67d;
    if (iVar2 != 0) {
      pcVar4 = s_sign_verify_fail_ffff0000907e89d3;
    }
    iVar2 = (uint)(iVar2 != 0) << 3;
  }
  FUN_ffff00009077de54(pcVar4);
  return iVar2;
}


