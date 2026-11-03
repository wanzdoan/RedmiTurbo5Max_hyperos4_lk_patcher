
void FUN_ffff0000be028940(void)

{
  long lVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  long lVar5;
  
  iVar2 = FUN_ffff0000be0283e0();
  lVar3 = DAT_ffff0000be0d6618;
  pcVar4 = DAT_ffff0000be0d6610;
  lVar1 = DAT_ffff0000be0d6608;
  if ((-1 < iVar2) &&
     (FUN_ffff0000be05fd98(s__PART_UTILS__dump_partition_tabl_ffff0000be0bf7ac,DAT_ffff0000be0d6624)
     , 0 < *(int *)(lVar1 + 0x50))) {
    lVar5 = 0;
    do {
      if ((*pcVar4 == '\0') && (pcVar4[1] == '\0')) {
        return;
      }
      FUN_ffff0000be05fd98
                (s__GPT___s___llu___llu___s_ffff0000be0b650f,lVar3,*(undefined8 *)(pcVar4 + 0x20),
                 *(undefined8 *)(pcVar4 + 0x28),lVar3 + 0x48);
      lVar5 = lVar5 + 1;
      lVar3 = lVar3 + 0x6d;
      pcVar4 = pcVar4 + 0x80;
    } while (lVar5 < *(int *)(lVar1 + 0x50));
  }
  return;
}


