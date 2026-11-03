// ffff000090789244  size=352  FUN_ffff000090789244

int FUN_ffff000090789244(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  ulong uVar2;
  int iVar3;
  uint uVar4;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_18;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  local_18 = 0x100;
  iVar1 = FUN_ffff0000907a0634
                    (param_1,param_2,&local_130,&local_18,&DAT_ffff0000908121af,3,
                     &DAT_ffff0000908121b2,0x100);
  if (iVar1 == 0) {
    if ((local_130._1_1_ == '\x01') && (1 < (int)local_18 + -1)) {
      uVar4 = 0;
      uVar2 = (ulong)&local_130 | 3;
      iVar1 = (int)local_18 + -3;
      do {
        if (*(char *)(uVar2 - 1) != -1) {
          if (*(char *)(uVar2 - 1) == '\0') {
            iVar3 = -1;
            if ((((iVar1 != -1) && (7 < uVar4)) && (iVar1 < 0x201)) &&
               (FUN_ffff00009077e858(param_3,uVar2,iVar1), iVar3 = iVar1, -1 < iVar1)) {
              return iVar1;
            }
            goto LAB_ffff000090789374;
          }
          break;
        }
        uVar4 = uVar4 + 1;
        uVar2 = uVar2 + 1;
        iVar1 = iVar1 + -1;
      } while (iVar1 != -1);
    }
    iVar3 = -1;
LAB_ffff000090789374:
    FUN_ffff00009077de54(s_RSA_padding_check_PKCS1_type_1_f_ffff0000907e6c56,iVar3);
    iVar1 = -2;
  }
  else {
    FUN_ffff00009077de54(s_sec_rsa_exptmod_failed_ret__d_ffff0000907fe198,iVar1);
    iVar1 = -1;
  }
  return iVar1;
}


