
undefined8 FUN_ffff0000be0171a8(char *param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined8 uVar3;
  long lVar4;
  char *pcVar5;
  char *pcVar6;
  char *pcVar7;
  ulong uVar8;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined4 local_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_48;
  
  local_48 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_60 = 0;
  local_70 = 0;
  uStack_68 = 0;
  if (param_1 != (char *)0x0) {
    do {
      pcVar7 = param_1;
      param_1 = pcVar7 + 1;
    } while (*pcVar7 == '\n');
    pcVar5 = pcVar7 + 1;
    if (*pcVar7 != '\0') {
      do {
        pcVar6 = pcVar5;
        pcVar5 = pcVar6 + 1;
        if (*pcVar6 == '\0') {
          pcVar5 = (char *)0x0;
          goto LAB_ffff0000be0171f4;
        }
      } while (*pcVar6 != '\n');
      *pcVar6 = '\0';
      goto LAB_ffff0000be0171f4;
    }
  }
  pcVar5 = (char *)0x0;
  pcVar7 = (char *)0x0;
LAB_ffff0000be0171f4:
  uVar3 = FUN_ffff0000be060e5c(s_CRC_LIST_ffff0000be0a5835);
  iVar1 = FUN_ffff0000be060778(pcVar7,s_CRC_LIST_ffff0000be0a5835,uVar3);
  if (iVar1 == 0) {
    if (pcVar5 != (char *)0x0) {
      do {
        pcVar7 = pcVar5;
        pcVar5 = pcVar7 + 1;
      } while (*pcVar7 == '\n');
      if (*pcVar7 != '\0') {
        do {
          pcVar6 = pcVar5;
          pcVar5 = pcVar6 + 1;
          if (*pcVar6 == '\0') {
            pcVar5 = (char *)0x0;
            goto LAB_ffff0000be017330;
          }
        } while (*pcVar6 != '\n');
        *pcVar6 = '\0';
LAB_ffff0000be017330:
        uVar8 = 0;
        while( true ) {
          iVar1 = FUN_ffff0000be0170ec(pcVar7,&local_58,&local_70);
          if (iVar1 != 0) {
            DAT_ffff0000be10872c = 0;
            return 1;
          }
          lVar4 = FUN_ffff0000be060e5c(&local_58);
          FUN_ffff0000be060eb8(&DAT_ffff0000be10842c + uVar8 * 0x18,&local_58,lVar4 + 1);
          uVar2 = FUN_ffff0000be05f5e0(&local_70,0,0x10);
          (&DAT_ffff0000be108440)[uVar8 * 6] = uVar2;
          FUN_ffff0000be05fd98
                    (s_crc_list__d__partition_name__s_v_ffff0000be0c608a,uVar8 & 0xffffffff,
                     &DAT_ffff0000be10842c + uVar8 * 0x18);
          if (pcVar5 == (char *)0x0) break;
          do {
            pcVar7 = pcVar5;
            pcVar5 = pcVar7 + 1;
          } while (*pcVar7 == '\n');
          if (*pcVar7 == '\0') break;
          do {
            pcVar6 = pcVar5;
            pcVar5 = pcVar6 + 1;
            if (*pcVar6 == '\0') {
              pcVar5 = (char *)0x0;
              goto LAB_ffff0000be0173d4;
            }
          } while (*pcVar6 != '\n');
          *pcVar6 = '\0';
LAB_ffff0000be0173d4:
          uVar8 = uVar8 + 1;
          if (uVar8 == 0x20) {
            DAT_ffff0000be10872c = 0;
            return 1;
          }
        }
      }
    }
    uVar3 = 0;
    DAT_ffff0000be10872c = 1;
  }
  else {
    DAT_ffff0000be10872c = 0;
    uVar3 = 1;
    DAT_ffff0000be108440 = 0;
    DAT_ffff0000be108458 = 0;
    DAT_ffff0000be108470 = 0;
    DAT_ffff0000be108488 = 0;
    DAT_ffff0000be1084a0 = 0;
    DAT_ffff0000be1084b8 = 0;
    DAT_ffff0000be1084d0 = 0;
    DAT_ffff0000be1084e8 = 0;
    DAT_ffff0000be108500 = 0;
    DAT_ffff0000be108518 = 0;
    DAT_ffff0000be108530 = 0;
    DAT_ffff0000be108548 = 0;
    DAT_ffff0000be108560 = 0;
    DAT_ffff0000be108578 = 0;
    DAT_ffff0000be108590 = 0;
    DAT_ffff0000be1085a8 = 0;
    DAT_ffff0000be1085c0 = 0;
    DAT_ffff0000be1085d8 = 0;
    DAT_ffff0000be1085f0 = 0;
    DAT_ffff0000be108608 = 0;
    DAT_ffff0000be108620 = 0;
    DAT_ffff0000be108638 = 0;
    DAT_ffff0000be108650 = 0;
    DAT_ffff0000be108668 = 0;
    DAT_ffff0000be108680 = 0;
    DAT_ffff0000be108698 = 0;
    DAT_ffff0000be1086b0 = 0;
    DAT_ffff0000be1086c8 = 0;
    DAT_ffff0000be1086e0 = 0;
    DAT_ffff0000be1086f8 = 0;
    DAT_ffff0000be108710 = 0;
    DAT_ffff0000be108728 = 0;
  }
  return uVar3;
}


