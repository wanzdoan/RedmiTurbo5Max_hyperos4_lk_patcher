
undefined8 FUN_ffff00009070df58(char *param_1)

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
          goto LAB_ffff00009070dfa4;
        }
      } while (*pcVar6 != '\n');
      *pcVar6 = '\0';
      goto LAB_ffff00009070dfa4;
    }
  }
  pcVar5 = (char *)0x0;
  pcVar7 = (char *)0x0;
LAB_ffff00009070dfa4:
  uVar3 = FUN_ffff00009077f250(s_CRC_LIST_ffff0000907d25a8);
  iVar1 = FUN_ffff00009077eae8(pcVar7,s_CRC_LIST_ffff0000907d25a8,uVar3);
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
            goto LAB_ffff00009070e0e0;
          }
        } while (*pcVar6 != '\n');
        *pcVar6 = '\0';
LAB_ffff00009070e0e0:
        uVar8 = 0;
        while( true ) {
          iVar1 = FUN_ffff00009070de9c(pcVar7,&local_58,&local_70);
          if (iVar1 != 0) {
            DAT_ffff00009083dfbc = 0;
            return 1;
          }
          lVar4 = FUN_ffff00009077f250(&local_58);
          FUN_ffff00009077f2ac(&DAT_ffff00009083dcbc + uVar8 * 0x18,&local_58,lVar4 + 1);
          uVar2 = FUN_ffff00009077d938(&local_70,0,0x10);
          (&DAT_ffff00009083dcd0)[uVar8 * 6] = uVar2;
          FUN_ffff00009077e11c
                    (s_crc_list__d__partition_name__s_v_ffff000090801eea,uVar8 & 0xffffffff,
                     &DAT_ffff00009083dcbc + uVar8 * 0x18);
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
              goto LAB_ffff00009070e184;
            }
          } while (*pcVar6 != '\n');
          *pcVar6 = '\0';
LAB_ffff00009070e184:
          uVar8 = uVar8 + 1;
          if (uVar8 == 0x20) {
            DAT_ffff00009083dfbc = 0;
            return 1;
          }
        }
      }
    }
    uVar3 = 0;
    DAT_ffff00009083dfbc = 1;
  }
  else {
    DAT_ffff00009083dfbc = 0;
    uVar3 = 1;
    DAT_ffff00009083dcd0 = 0;
    DAT_ffff00009083dce8 = 0;
    DAT_ffff00009083dd00 = 0;
    DAT_ffff00009083dd18 = 0;
    DAT_ffff00009083dd30 = 0;
    DAT_ffff00009083dd48 = 0;
    DAT_ffff00009083dd60 = 0;
    DAT_ffff00009083dd78 = 0;
    DAT_ffff00009083dd90 = 0;
    DAT_ffff00009083dda8 = 0;
    DAT_ffff00009083ddc0 = 0;
    DAT_ffff00009083ddd8 = 0;
    DAT_ffff00009083ddf0 = 0;
    DAT_ffff00009083de08 = 0;
    DAT_ffff00009083de20 = 0;
    DAT_ffff00009083de38 = 0;
    DAT_ffff00009083de50 = 0;
    DAT_ffff00009083de68 = 0;
    DAT_ffff00009083de80 = 0;
    DAT_ffff00009083de98 = 0;
    DAT_ffff00009083deb0 = 0;
    DAT_ffff00009083dec8 = 0;
    DAT_ffff00009083dee0 = 0;
    DAT_ffff00009083def8 = 0;
    DAT_ffff00009083df10 = 0;
    DAT_ffff00009083df28 = 0;
    DAT_ffff00009083df40 = 0;
    DAT_ffff00009083df58 = 0;
    DAT_ffff00009083df70 = 0;
    DAT_ffff00009083df88 = 0;
    DAT_ffff00009083dfa0 = 0;
    DAT_ffff00009083dfb8 = 0;
  }
  return uVar3;
}


