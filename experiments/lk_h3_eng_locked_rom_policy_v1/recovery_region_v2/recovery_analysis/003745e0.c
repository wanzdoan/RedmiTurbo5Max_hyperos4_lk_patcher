
void set_reboot_message(void)

{
  long lVar1;
  int iVar2;
  undefined4 uVar3;
  string *psVar4;
  ulong uVar5;
  undefined4 *puVar6;
  ostream *poVar7;
  FILE *__stream;
  int *piVar8;
  size_t sVar9;
  ulong extraout_x1;
  char *pcVar10;
  string *psVar11;
  undefined4 uVar12;
  LogMessage aLStack_78 [8];
  ulong local_70 [2];
  void *local_60;
  undefined8 uStack_58;
  long local_48;

  lVar1 = tpidr_el0;
                    /* try { // try from 00374600 to 0047461f has its CatchHandler @ 003749f4 */
  local_48 = *(long *)(lVar1 + 0x28);
                    /* try { // try from 00374620 to 00474663 has its CatchHandler @ 00374a00 */
  local_70[0] = CONCAT17(local_70[0]._7_1_,0x6373696d2f0a);
  psVar4 = (string *)volume_for_mount_point((string *)local_70);
  if ((local_70[0] & 1) != 0) {
    operator_delete(local_60);
  }
  if (psVar4 == (string *)0x0) {
    uVar5 = android::base::ShouldLog(4,0);
    if ((uVar5 & 1) != 0) {
      puVar6 = (undefined4 *)__errno();
      uVar12 = *puVar6;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_70,"bootable/recovery/miutil/mi_system.cpp",0x66,4,0,0xffffffff
                );
      poVar7 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_70);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar7,"Cannot load volume /misc!",0x19);
LAB_0037479c:
      android::base::LogMessage::~LogMessage((LogMessage *)local_70);
      uVar3 = 0xffffffff;
      *puVar6 = uVar12;
      goto LAB_00374a0c;
    }
  }
  else {
    psVar11 = *(string **)(psVar4 + 0x70);
    if (((byte)psVar4[0x60] & 1) == 0) {
      psVar11 = psVar4 + 0x61;
    }
                    /* try { // try from 00374664 to 0047478f has its CatchHandler @ 00374550 */
    iVar2 = strcmp((char *)psVar11,"mtd");
    if (iVar2 == 0) {
      uVar5 = android::base::ShouldLog(4,0);
      if ((uVar5 & 1) != 0) {
        puVar6 = (undefined4 *)__errno();
        uVar12 = *puVar6;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)local_70,"bootable/recovery/miutil/mi_system.cpp",0x6a,4,0,
                   0xffffffff);
        poVar7 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_70);
                    /* try { // try from 00374790 to 004747af has its CatchHandler @ 00374a04 */
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar7,"Set reboot message on mtd not supported!",0x28);
        goto LAB_0037479c;
      }
    }
    else {
      iVar2 = strcmp((char *)psVar11,"emmc");
      if (iVar2 == 0) {
                    /* try { // try from 003747b0 to 004747e3 has its CatchHandler @ 00374a0c */
        psVar11 = *(string **)(psVar4 + 0x10);
        if (((byte)*psVar4 & 1) == 0) {
          psVar11 = psVar4 + 1;
        }
        __stream = fopen((char *)psVar11,"wb");
        if (__stream == (FILE *)0x0) {
          uVar5 = android::base::ShouldLog(4,0);
          if ((uVar5 & 1) != 0) {
            piVar8 = (int *)__errno();
                    /* try { // try from 00374890 to 004748d3 has its CatchHandler @ 003749f8 */
            iVar2 = *piVar8;
            android::base::LogMessage::LogMessage
                      (aLStack_78,"bootable/recovery/miutil/mi_system.cpp",0xcc,4,0,0xffffffff);
            poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_78);
            poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                               (poVar7,"Can\'t open ",0xb);
            poVar7 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                     operator<(poVar7,psVar4);
            poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                               (poVar7,": ",2);
            pcVar10 = strerror(*piVar8);
                    /* try { // try from 003748f4 to 00474913 has its CatchHandler @ 003749d0 */
            sVar9 = strlen(pcVar10);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar7,pcVar10,sVar9);
LAB_00374908:
            android::base::LogMessage::~LogMessage(aLStack_78);
            uVar3 = 0xffffffff;
                    /* try { // try from 00374914 to 0047495f has its CatchHandler @ 003749ec */
            *piVar8 = iVar2;
            goto LAB_00374a0c;
          }
        }
        else {
          pcVar10 = (char *)0x0;
          iVar2 = fseek(__stream,0x1000,0);
          if (iVar2 == 0) {
            local_70[1] = 0;
            local_70[0] = 0;
            uStack_58 = 0;
            local_60 = (void *)0x0;
            snprintf((char *)local_70,extraout_x1,pcVar10);
            sVar9 = fwrite(local_70,0x20,1,__stream);
            if ((int)sVar9 == 1) {
              iVar2 = fclose(__stream);
              uVar3 = 0;
              if (iVar2 == 0) goto LAB_00374a0c;
              uVar5 = android::base::ShouldLog(4,0);
              if ((uVar5 & 1) != 0) {
                piVar8 = (int *)__errno();
                iVar2 = *piVar8;
                    /* try { // try from 0037497c to 0047499b has its CatchHandler @ 003749c8 */
                android::base::LogMessage::LogMessage
                          (aLStack_78,"bootable/recovery/miutil/mi_system.cpp",0xde,4,0,0xffffffff);
                    /* try { // try from 0037499c to 004749b3 has its CatchHandler @ 003749d4 */
                poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_78);
                poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                   (poVar7,"Failed closing ",0xf);
                    /* try { // try from 003749b4 to 00474a37 has its CatchHandler @ 00374550 */
                poVar7 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                         char_traits<char>,std::operator<(poVar7,psVar4);
                poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                   (poVar7,": ",2);
                    /* catch() { ... } // from try @ 0037497c with catch @ 003749c8 */
                    /* catch() { ... } // from try @ 003748f4 with catch @ 003749d0 */
                pcVar10 = strerror(*piVar8);
                    /* catch() { ... } // from try @ 0037499c with catch @ 003749d4 */
                sVar9 = strlen(pcVar10);
                    /* catch() { ... } // from try @ 00374870 with catch @ 003749e8 */
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar7,pcVar10,sVar9);
                    /* catch() { ... } // from try @ 00374914 with catch @ 003749ec */
                goto LAB_00374908;
              }
              goto LAB_00374a08;
            }
                    /* catch() { ... } // from try @ 00374800 with catch @ 003749f0 */
                    /* catch() { ... } // from try @ 00374600 with catch @ 003749f4 */
                    /* catch() { ... } // from try @ 00374890 with catch @ 003749f8 */
            uVar5 = android::base::ShouldLog(4,0);
                    /* catch() { ... } // from try @ 00374820 with catch @ 003749fc */
            if ((uVar5 & 1) != 0) {
              piVar8 = (int *)__errno();
              iVar2 = *piVar8;
                    /* try { // try from 00374a40 to 00474ab7 has its CatchHandler @ 00374a40
                       catch() { ... } // from try @ 00374a40 with catch @ 00374a40
                       catch() { ... } // from try @ 00374af0 with catch @ 00374a40
                       catch() { ... } // from try @ 00374bd0 with catch @ 00374a40 */
              android::base::LogMessage::LogMessage
                        (aLStack_78,"bootable/recovery/miutil/mi_system.cpp",0xd9,4,0,0xffffffff);
              poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_78);
              poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                 (poVar7,"Failed writing ",0xf);
              poVar7 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std
                       ::operator<(poVar7,psVar4);
              poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                 (poVar7,": ",2);
              pcVar10 = strerror(*piVar8);
              sVar9 = strlen(pcVar10);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar7,pcVar10,sVar9);
              goto LAB_00374ab4;
            }
          }
          else {
            uVar5 = android::base::ShouldLog(4,0);
            if ((uVar5 & 1) != 0) {
              piVar8 = (int *)__errno();
              iVar2 = *piVar8;
                    /* try { // try from 00374800 to 0047481f has its CatchHandler @ 003749f0 */
              android::base::LogMessage::LogMessage
                        (aLStack_78,"bootable/recovery/miutil/mi_system.cpp",0xd1,4,0,0xffffffff);
                    /* try { // try from 00374820 to 00474853 has its CatchHandler @ 003749fc */
              poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_78);
              poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                 (poVar7,"Failed seeking ",0xf);
              poVar7 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std
                       ::operator<(poVar7,psVar4);
              poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                 (poVar7,": ",2);
              pcVar10 = strerror(*piVar8);
              sVar9 = strlen(pcVar10);
                    /* try { // try from 00374870 to 0047488f has its CatchHandler @ 003749e8 */
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar7,pcVar10,sVar9);
LAB_00374ab4:
                    /* try { // try from 00374ab8 to 00474ad7 has its CatchHandler @ 00374bf0 */
              android::base::LogMessage::~LogMessage(aLStack_78);
              *piVar8 = iVar2;
            }
          }
                    /* catch() { ... } // from try @ 00374620 with catch @ 00374a00 */
                    /* catch() { ... } // from try @ 00374790 with catch @ 00374a04 */
          fclose(__stream);
        }
      }
      else {
        uVar5 = android::base::ShouldLog(4,0);
        if ((uVar5 & 1) != 0) {
          puVar6 = (undefined4 *)__errno();
          uVar12 = *puVar6;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)local_70,"bootable/recovery/miutil/mi_system.cpp",0x6f,4,0,
                     0xffffffff);
          poVar7 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_70);
          poVar7 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                             (poVar7,"unknown misc partition fs_type ",0x1f);
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar7,psVar4 + 0x60);
          android::base::LogMessage::~LogMessage((LogMessage *)local_70);
          uVar3 = 0xffffffff;
          *puVar6 = uVar12;
          goto LAB_00374a0c;
        }
      }
    }
  }
LAB_00374a08:
  uVar3 = 0xffffffff;
LAB_00374a0c:
                    /* catch() { ... } // from try @ 003747b0 with catch @ 00374a0c */
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar3);
}
