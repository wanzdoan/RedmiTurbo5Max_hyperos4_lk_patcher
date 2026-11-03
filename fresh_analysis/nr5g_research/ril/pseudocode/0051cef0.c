
/* RmcDcCommonReqHandler::updateNrQos(int) */

void __thiscall RmcDcCommonReqHandler::updateNrQos(RmcDcCommonReqHandler *this,int param_1)

{
  undefined4 uVar1;
  long lVar2;
  int iVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  undefined4 uVar7;
  undefined4 uVar8;
  undefined4 uVar9;
  ulong uVar10;
  ulong uVar11;
  undefined8 uVar12;
  undefined4 *puVar13;
  char *pcVar14;
  RfxAtLine *this_00;
  undefined8 local_80;
  int local_74;
  RfxAtResponse *local_70;
  long local_68;
  
  uVar11 = (ulong)(uint)param_1;
  lVar2 = tpidr_el0;
  local_68 = *(long *)(lVar2 + 0x28);
  if ((param_1 < 0) || (iVar3 = getPdnTableSize(this), iVar3 <= param_1)) {
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) == 0) {
      mtkLogW("RmcDcCommon","[%d][%s] invalid aid%d",*(undefined4 *)(this + 8),"updateNrQos",uVar11)
      ;
      uVar12 = 0;
    }
    else {
      local_70 = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",&local_70,&DAT_001fbebf,"RmcDcCommon");
      mtkLogW(local_70,"[%d][%s] invalid aid%d",*(undefined4 *)(this + 8),"updateNrQos",uVar11);
      android::String8::~String8((String8 *)&local_70);
      uVar12 = 0;
    }
    goto LAB_0051d400;
  }
  uVar1 = *(undefined4 *)(this + 8);
  local_74 = 0;
  local_70 = (RfxAtResponse *)0x0;
  android::String8::format("AT+C5GQOSRDP=%d",&local_80,uVar11);
  RfxBaseHandler::atSendCommandMultiline((String8 *)this,(char *)&local_80,"+C5GQOSRDP:");
  android::String8::~String8((String8 *)&local_80);
  if (local_70 == (RfxAtResponse *)0x0) {
    uVar11 = __rfx_is_gt_mode();
    if ((uVar11 & 1) == 0) {
      pcVar14 = "[%d][%s] fail to get p_response!";
      goto LAB_0051d3c8;
    }
    local_80 = 0;
    android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
    pcVar14 = "[%d][%s] fail to get p_response!";
    goto LAB_0051d394;
  }
  uVar10 = RfxAtResponse::isAtResponseFail(local_70);
  if ((uVar10 & 1) == 0) {
    this_00 = *(RfxAtLine **)(local_70 + 0x20);
    if (this_00 == (RfxAtLine *)0x0) {
      uVar11 = __rfx_is_gt_mode();
      if ((uVar11 & 1) == 0) {
        pcVar14 = "[%d][%s] p_cur is NULL";
LAB_0051d3c8:
        mtkLogE("RmcDcCommon",pcVar14,uVar1,"updateNrQos");
        goto LAB_0051d3d8;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] p_cur is NULL";
LAB_0051d394:
      mtkLogE(local_80,pcVar14,uVar1,"updateNrQos");
LAB_0051d3a4:
      android::String8::~String8((String8 *)&local_80);
      goto LAB_0051d3d8;
    }
    RfxAtLine::atTokStart(this_00,&local_74);
    if (local_74 < 0) {
      uVar11 = __rfx_is_gt_mode();
      if ((uVar11 & 1) == 0) {
        pcVar14 = "[%d][%s] ERROR occurs when token start";
        goto LAB_0051d3c8;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] ERROR occurs when token start";
      goto LAB_0051d394;
    }
    iVar3 = RfxAtLine::atTokNextint(this_00,&local_74);
    if (local_74 < 0) {
      uVar11 = __rfx_is_gt_mode();
      if ((uVar11 & 1) == 0) {
        pcVar14 = "[%d][%s] ERROR occurs when parsing response aid";
        goto LAB_0051d3c8;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] ERROR occurs when parsing response aid";
      goto LAB_0051d394;
    }
    if (iVar3 != param_1) {
      uVar10 = __rfx_is_gt_mode();
      if ((uVar10 & 1) == 0) {
        pcVar14 = "[%d][%s] aid%d is different from MD assigned aid%d";
        goto LAB_0051d340;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] aid%d is different from MD assigned aid%d";
      goto LAB_0051d2ec;
    }
    uVar4 = RfxAtLine::atTokNextint(this_00,&local_74);
    if (local_74 < 0) {
      uVar11 = __rfx_is_gt_mode();
      if ((uVar11 & 1) == 0) {
        pcVar14 = "[%d][%s] parsing 5QI fail";
        goto LAB_0051d3c8;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] parsing 5QI fail";
      goto LAB_0051d394;
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 == 0) {
      uVar5 = 0;
    }
    else {
      uVar5 = RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no dl_gfbr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no dl_gfbr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 == 0) {
      uVar6 = 0;
    }
    else {
      uVar6 = RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no ul_gfbr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no ul_gfbr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 == 0) {
      uVar7 = 0;
    }
    else {
      uVar7 = RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no dl_mfbr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no dl_mfbr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 == 0) {
      uVar8 = 0;
    }
    else {
      uVar8 = RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no ul_mfbr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no ul_mfbr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 != 0) {
      RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no dl_sambr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no dl_sambr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 != 0) {
      RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no ul_sambr",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no ul_sambr",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    iVar3 = RfxAtLine::atTokHasmore(this_00);
    if (iVar3 == 0) {
      uVar9 = 0;
    }
    else {
      uVar9 = RfxAtLine::atTokNextint(this_00,&local_74);
      if (local_74 < 0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          mtkLogE("RmcDcCommon","[%d][%s] no averagingWindow",uVar1,"updateNrQos");
        }
        else {
          local_80 = 0;
          android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
          mtkLogE(local_80,"[%d][%s] no averagingWindow",uVar1,"updateNrQos");
          android::String8::~String8((String8 *)&local_80);
        }
      }
    }
    uVar11 = RmcDcPdnManager::isDedicateBearer(*(RmcDcPdnManager **)(this + 0x18),param_1);
    if ((uVar11 & 1) == 0) {
      puVar13 = (undefined4 *)
                RmcDcPdnManager::getDefaultQos(*(RmcDcPdnManager **)(this + 0x18),param_1);
      if (puVar13 == (undefined4 *)0x0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          pcVar14 = "[%d][%s] defaultQos is NULL";
          goto LAB_0051d90c;
        }
        local_80 = 0;
        android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
        pcVar14 = "[%d][%s] defaultQos is NULL";
        goto LAB_0051d8d4;
      }
      puVar13[6] = uVar9;
      *puVar13 = uVar4;
      puVar13[1] = uVar7;
      puVar13[4] = uVar6;
      uVar12 = 1;
      puVar13[2] = uVar5;
      puVar13[3] = uVar8;
    }
    else {
      puVar13 = (undefined4 *)
                RmcDcPdnManager::getQosSession(*(RmcDcPdnManager **)(this + 0x18),param_1);
      if (puVar13 == (undefined4 *)0x0) {
        uVar11 = __rfx_is_gt_mode();
        if ((uVar11 & 1) == 0) {
          pcVar14 = "[%d][%s] qosSession is NULL";
LAB_0051d90c:
          mtkLogW("RmcDcCommon",pcVar14,uVar1,"updateNrQos");
          goto LAB_0051d3d8;
        }
        local_80 = 0;
        android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
        pcVar14 = "[%d][%s] qosSession is NULL";
LAB_0051d8d4:
        mtkLogW(local_80,pcVar14,uVar1,"updateNrQos");
        goto LAB_0051d3a4;
      }
      puVar13[7] = uVar9;
      puVar13[1] = uVar4;
      puVar13[2] = uVar7;
      puVar13[3] = uVar5;
      puVar13[4] = uVar8;
      uVar12 = 1;
      puVar13[5] = uVar6;
      puVar13[6] = *puVar13;
    }
  }
  else {
    if (0 < *(int *)(local_70 + 0xc)) {
      uVar11 = __rfx_is_gt_mode();
      if ((uVar11 & 1) == 0) {
        pcVar14 = 
        "[%d][%s] weird case, can not get fail cause due to final response indicates success";
        goto LAB_0051d3c8;
      }
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = 
      "[%d][%s] weird case, can not get fail cause due to final response indicates success";
      goto LAB_0051d394;
    }
    iVar3 = RfxAtResponse::atGetCmeError(local_70);
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) != 0) {
      local_80 = 0;
      android::String8::format("%s%s",&local_80,&DAT_001fbebf,"RmcDcCommon");
      pcVar14 = "[%d][%s] AT+C5GQOSRDP for aid%d returns cme cause : %d";
LAB_0051d2ec:
      mtkLogE(local_80,pcVar14,uVar1,"updateNrQos",uVar11,iVar3);
      goto LAB_0051d3a4;
    }
    pcVar14 = "[%d][%s] AT+C5GQOSRDP for aid%d returns cme cause : %d";
LAB_0051d340:
    mtkLogE("RmcDcCommon",pcVar14,uVar1,"updateNrQos",uVar11,iVar3);
LAB_0051d3d8:
    uVar12 = 0;
  }
  if (local_70 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_70 + *(long *)(*(long *)local_70 + -0x18)),&local_70);
  }
LAB_0051d400:
  if (*(long *)(lVar2 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar12);
}


